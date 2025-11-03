/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.model.db1.BabStatus;
import com.advantech.service.db1.BabService;
import com.advantech.service.db1.BabSettingHistoryService;
import com.advantech.service.db1.SqlViewService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import javax.validation.constraints.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Justin.Yeh
 */
public class HandleUncloseBabProcess extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(HandleUncloseBabProcess.class);

    private final BabService babService;

    private final BabSettingHistoryService babSettingHistoryService;

    private final SqlViewService viewService;

    private final PropertiesReader p;

    private final Boolean isAutoSave;

    private final int devLineId = 7;

    public HandleUncloseBabProcess() {
        babService = (BabService) ApplicationContextHelper.getBean("babService");
        babSettingHistoryService = (BabSettingHistoryService) ApplicationContextHelper.getBean("babSettingHistoryService");
        viewService = (SqlViewService) ApplicationContextHelper.getBean("sqlViewService");
        p = (PropertiesReader) ApplicationContextHelper.getBean("propertiesReader");
        this.isAutoSave = p.getIsBabNotPreAutoSave();
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        saveUnclosedBab();
    }

    private void saveUnclosedBab() {
        List<Bab> processingBabs = babService.findProcessing();
        List<BabSettingHistory> processingBabSettings = babSettingHistoryService.findProcessingWithBabAndTagName();

        processingBabs.forEach((bab) -> {
            int babId = bab.getId();

            List<BabSettingHistory> babSettings = processingBabSettings.stream()
                    .filter(rec -> rec.getBab().getId() == babId).collect(toList());

            int babLineId = bab.getLine().getId();
            int babPeople = bab.getPeople();
            if (!babSettings.isEmpty() && babSettings.size() != babPeople
                    && isAutoSave && babLineId != devLineId //comment for debugging
                    ) {
                Map<String, Integer> unclosedMap = getUnclosedMap(bab, babSettings);

                babSettings.forEach((setting) -> {
                    String tagName = setting.getTagName().getName();
                    int settingStation = setting.getStation();

                    if (bab.getIspre() != 1 && unclosedMap.containsKey(tagName) && settingStation > 2) {
                        babService.autoCloseNotPre(bab, setting); // need to call public method from other class to flush session.

                        if (settingStation == babPeople) {
                            babService.changeBabStatusFollowCloseBab(babId, BabStatus.AUTO_CLOSED); // change status after flush.
                        }
                    }
                });
            }
        });

    }

    private Map<String, Integer> getUnclosedMap(Bab b, @NotNull List<BabSettingHistory> processSettings) {
        Map<String, Integer> unclosedMap = new HashMap<>();
        if (processSettings.size() != b.getPeople()) {
            unclosedMap = viewService.getUnclosedLineStation(b.getId());
        }
        return unclosedMap;
    }
}
