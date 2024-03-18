/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 停止sensor分配組別用，中間假使組別已經不同步，
 * 至少換到下套的同步率又會被初始化(停止sensor時組別就不會再分配直到database有下一套工單id出現)
 */
package com.advantech.controller;

import com.advantech.endpoint.Endpoint6;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabAlarmHistory;
import com.advantech.model.db1.BabPreAssyPcsRecord;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.model.db1.ReplyStatus;
import com.advantech.service.db1.BabAlarmHistoryService;
import com.advantech.service.db1.BabPreAssyPcsRecordService;
import com.advantech.service.db1.BabSensorLoginRecordService;
import com.advantech.service.db1.BabSettingHistoryService;
import com.advantech.service.db1.BabService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import static com.google.common.base.Preconditions.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/BabOtherStationController")
public class BabOtherStationController {

    private static final Logger log = LoggerFactory.getLogger(BabOtherStationController.class);

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private BabAlarmHistoryService babAlarmHistoryService;

    @Autowired
    private BabService babService;

    @Autowired
    private BabPreAssyPcsRecordService babPreAssyPcsRecordService;

    @Autowired
    private Endpoint6 ep6;

    @RequestMapping(value = "/changeUser", method = {RequestMethod.POST})
    @ResponseBody
    protected String changeUser(
            @RequestParam String jobnumber,
            @RequestParam String tagName
    ) throws Exception {
        babSensorLoginRecordService.changeUser(jobnumber, tagName);
        return "success";
    }

    @RequestMapping(value = "/stationComplete", method = {RequestMethod.POST})
    @ResponseBody
    protected String stationComplete(
            @RequestParam int bab_id,
            @RequestParam String tagName,
            @RequestParam String jobnumber,
            @RequestParam(required = false) Integer pcs
    ) {
        Bab b = babService.findByPrimaryKey(bab_id);
        BabSettingHistory setting = babSettingHistoryService.findFirstProcessingByTagName(tagName);
        checkArgument(setting != null, "找不到該站使用者");
        checkArgument(setting.getLastUpdateTime() == null, "感應器已經關閉");
        checkStation(b, setting.getStation());

        if (setting.getStation() == b.getPeople()) { // if the station is the last station
            //Save pre pcs record first
            if (b.getIspre() == 1) {
                List<BabSettingHistory> settings = babSettingHistoryService.findByBab(b);

                List<BabPreAssyPcsRecord> pcsRecords = new ArrayList();
                settings.forEach((bsh) -> {
                    pcsRecords.add(new BabPreAssyPcsRecord(bsh, pcs));
                });
                babPreAssyPcsRecordService.insert(pcsRecords);
            }

            this.closeBab(b, bab_id);

            //If not preAssy, refresh endpoint data when user finished the job
            if (b.getIspre() == 0) {
                ep6.syncAndEcho();
            }
        } else {
            babService.stationComplete(b, setting);
        }

        return "success";
    }

    private void closeBab(Bab b, int bab_id) {
        babService.closeBab(b);

        BabAlarmHistory bah = babAlarmHistoryService.findByBab(bab_id);
        if (bah != null && bah.getTotalPcs() < 10) {
            //Get object again and set reply flag
            //Get bab again because object bab close by procedure not by update object itself
            //bab object is old, babStatus is null
            b = babService.findByPrimaryKey(bab_id);
            b.setReplyStatus(ReplyStatus.NO_NEED_TO_REPLY);
            babService.update(b);
        }
    }

    // call by quartzJob
    public void autoCloseNotPre(Bab b) {
        if (b.getIspre() == 1) {
            return;
        }

        int bab_id = b.getId();
        List<BabSettingHistory> settings = babSettingHistoryService.findByBab(b);

        for (BabSettingHistory setting : settings) {
            if (setting.getLastUpdateTime() == null) {
                if (setting.getStation() == b.getPeople()) {
                    this.closeBab(b, bab_id);
                } else if (setting.getStation() > 1) {
                    babService.stationComplete(b, setting);
                }
            }
        }
    }

    private void checkStation(Bab b, int station) {
        checkArgument(station <= b.getPeople(), "所在站別大於本工單所輸入的人數，請重新確認");
    }

}
