/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 幫忙把BAB資料在晚上10點時記錄到資料庫(晚上6點的job 只關閉燈號 txt 1 -> 0)
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabStatus;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.DatetimeGenerator;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.service.db1.BabService;
import com.advantech.service.db1.BabSettingHistoryService;
import java.util.List;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Wei.Cheng Auto close bab and set to abnormal sign on bab isused
 * 避免Auto close on working time, 使用者無法得知系統已經自動關閉逾期未關閉工單
 */
public class HandleUncloseBab extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(HandleUncloseBab.class);

    private final BabService babService;

    private final BabSettingHistoryService babSettingHistoryService;

    public HandleUncloseBab() {
        babService = (BabService) ApplicationContextHelper.getBean("babService");
        babSettingHistoryService = (BabSettingHistoryService) ApplicationContextHelper.getBean("babSettingHistoryService");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        saveBABData();
    }

    private void saveBABData() {
        DatetimeGenerator ge = new DatetimeGenerator("yyyy-MM-dd HH:mm");
        log.info("Current time: " + ge.dateFormatToString(new DateTime()));

        List<Bab> unClosedBabs = this.getUnclosedBabs();
        List<BabSettingHistory> allBabSettings = this.getUnclosedSettings();
        log.info("Unclosed babList size = " + unClosedBabs.size());

        for (Bab bab : unClosedBabs) {
            int babId = bab.getId();
            log.info("Begin autoSave unclose bab " + babId);
            if (bab.getIspre() == 1) {
                babService.closeBabDirectly(bab);
            } else {
                List<BabSettingHistory> babSettings = allBabSettings.stream()
                        .filter(setting -> setting.getBab().getId() == babId).collect(Collectors.toList());
                babService.autoCloseNotPreByJob(bab, babSettings);
            }

            babService.changeBabStatusFollowCloseBab(babId, BabStatus.UNFINSHED);
            log.info("Close autoSave bab status success");
        }
    }

    private List getUnclosedBabs() {
        return babService.findProcessing();
    }

    private List getUnclosedSettings() {
        return babSettingHistoryService.findProcessingWithBabAndTagName();
    }
}
