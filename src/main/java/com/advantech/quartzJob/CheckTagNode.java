/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.MailManager;
import com.advantech.model.db1.User;
import com.advantech.service.db1.SqlViewService;
import com.advantech.service.db1.UserService;
import com.advantech.webapi.WaGetTagValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Justin.Yeh
 */
public class CheckTagNode extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(CheckTagNode.class);

    private final SqlViewService sqlViewService;

    private final WaGetTagValue waGetTagValue;

    private static Set<String> liveTagNames, leakTagNames, moreTagNames;

    private final MailManager mailManager;

    private final UserService userService;

    private Map<String, String> _DIDOSysTagMap;

    public CheckTagNode() {
        sqlViewService = (SqlViewService) ApplicationContextHelper.getBean("sqlViewService");
        waGetTagValue = (WaGetTagValue) ApplicationContextHelper.getBean("waGetTagValue");
        mailManager = (MailManager) ApplicationContextHelper.getBean("mailManager");
        userService = (UserService) ApplicationContextHelper.getBean("userService");
        if (liveTagNames == null) {
            liveTagNames = getDIDOValueMap().keySet();
        }
    }

    private Map<String, Integer> getDIDOValueMap() {
        List<Map> sensorDIDOs = sqlViewService.findSensorDIDONames();
        String didoKey = sqlViewService.getVwDiDoColumn();
        String sysTagKey = sqlViewService.getVwSysTagName();

        _DIDOSysTagMap = sensorDIDOs.stream()
                .collect(Collectors.toMap(
                        m -> (String) m.getOrDefault(didoKey, ""),
                        m -> (String) m.getOrDefault(sysTagKey, ""),
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new
                ));

        return waGetTagValue.getMapByTagNames(new ArrayList<>(_DIDOSysTagMap.keySet()));
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {

        Map<String, Integer> map = getDIDOValueMap();
        Set<String> currentTagNames = map.keySet();

        leakTagNames = getDiffNames(liveTagNames, currentTagNames);
        moreTagNames = getDiffNames(currentTagNames, liveTagNames);
        if (!leakTagNames.isEmpty() || !moreTagNames.isEmpty()) {
            try {
                liveTagNames = currentTagNames;
                sendMail();

                Map<String, Integer> mapDO = map.entrySet().parallelStream()
                        .filter(e -> e.getKey().contains("DO"))
                        .collect(Collectors.toConcurrentMap(
                                e -> e.getKey(),
                                e -> e.getValue(),
                                (existingValue, newValue) -> existingValue));
                waGetTagValue.setMap(mapDO);
            } catch (MessagingException ex) {
                log.error(ex.toString());
            }
        }
    }

    private Set<String> getDiffNames(Set<String> src, Set<String> target) {
        // keySet is read-only, new HashSet() if need to write
        Set<String> srcCopy = new HashSet<>(src);
        Set<String> targetCopy = new HashSet<>(target);
        srcCopy.removeAll(targetCopy);
        return srcCopy;
    }

    private void sendMail() throws MessagingException {
        List<User> ccLoops = userService.findByUserNotification("admin_alarm_cc");
        List<User> to = userService.findByUserNotification("sensor_alarm");
        String subject = "[藍燈系統]Sensor異動通知";
        String mailBody = generateMailBody();
        mailManager.sendMail(to, ccLoops, subject, mailBody);
    }

    private String generateMailBody() {
        StringBuilder sb = new StringBuilder();

        sb.append("<p>時間 <strong>");
        sb.append(new Date());
        sb.append(" 上次檢查後，Adam 異動訊息如下</strong>");

        int registerNo = _DIDOSysTagMap.size();
        if (registerNo != liveTagNames.size()) {
            sb.append("<br/><span style='color:red'>Adam port 註冊總數 ");
            sb.append(registerNo);
            sb.append("筆，請協助確認是否正常，謝謝。</span>");
        }

        sb.append("</p><p style='color:red'>新增 : ");
        sb.append(findMapBySet(moreTagNames).toString());
        sb.append("</p><p style='color:red'>減少 : ");
        sb.append(findMapBySet(leakTagNames).toString());
        sb.append("</p><p>現存 <span style='color:red'>");
        sb.append(liveTagNames.size());
        sb.append("</span> 筆 : ");
        sb.append(sortSetByDefault(liveTagNames).toString());
        sb.append("</p>");

        return sb.toString();
    }

    private List<String> sortSetByDefault(Set<String> set) {
        List<String> sortedList = new ArrayList<>(set);
        sortedList.sort(null);
        return sortedList;
    }

    private Map<String, String> findMapBySet(Set<String> set) {
        return _DIDOSysTagMap.entrySet().stream()
                .filter(mm -> set.contains(mm.getKey()))
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue(),
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new));
    }
}
