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
import java.util.Date;
import java.util.HashSet;
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

    private static Set<String> liveTagNames;

    private final MailManager mailManager;

    private final UserService userService;

    public CheckTagNode() {
        sqlViewService = (SqlViewService) ApplicationContextHelper.getBean("sqlViewService");
        waGetTagValue = (WaGetTagValue) ApplicationContextHelper.getBean("waGetTagValue");
        mailManager = (MailManager) ApplicationContextHelper.getBean("mailManager");
        userService = (UserService) ApplicationContextHelper.getBean("userService");
        if (liveTagNames == null) {
            List<String> l = sqlViewService.findSensorDIDONames();
            liveTagNames = waGetTagValue.getMapByTagNames(l).keySet();
        }
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {

        List<String> l = sqlViewService.findSensorDIDONames();
        Map<String, Integer> map = waGetTagValue.getMapByTagNames(l);
        Set<String> currentTagNames = new HashSet<>(map.keySet());// keySet is read-only

        Set<String> leakTagNames = getDiffNames(liveTagNames, currentTagNames);
        Set<String> moreTagNames = getDiffNames(currentTagNames, liveTagNames);
        if (!leakTagNames.isEmpty() || !moreTagNames.isEmpty()) {
            try {
                sendMail(leakTagNames, moreTagNames);
                liveTagNames = currentTagNames;
                
                Map<String, Integer> mapDO = map.entrySet().parallelStream()
                        .filter(e -> e.getKey().contains("DO"))
                        .collect(Collectors.toConcurrentMap(e -> e.getKey(), e -> e.getValue()));
                waGetTagValue.setMap(mapDO);
            } catch (MessagingException ex) {
                log.error(ex.toString());
            }
        }
    }

    private Set<String> getDiffNames(Set<String> src, Set<String> target) {
        Set<String> srcCopy = new HashSet<>(src);
        Set<String> targetCopy = new HashSet<>(target);
        srcCopy.removeAll(targetCopy);
        return srcCopy;
    }

    private void sendMail(Set<String> leakTagNames, Set<String> moreTagNames) throws MessagingException {
        List<User> ccLoops = userService.findByUserNotification("admin_alarm_cc");
        List<User> to = ccLoops;
        String subject = "[藍燈系統]Sensor異常通知";
        String mailBody = generateMailBody(leakTagNames, moreTagNames);
        mailManager.sendMail(to, ccLoops, subject, mailBody);
    }

    private String generateMailBody(Set<String> leakTagNames, Set<String> moreTagNames) {
        return new StringBuilder()
                .append("<p>時間 <strong>")
                .append(new Date())
                .append(" Sensor異常訊息如下</strong></p>")
                .append("<p style='color:red'>新增:")
                .append(moreTagNames.toString())
                .append(" ;減少:")
                .append(leakTagNames.toString())
                .append("</p>")
                .append("<p>請協助確認感應器是否正常，謝謝。</p>")
                .toString();
    }
}
