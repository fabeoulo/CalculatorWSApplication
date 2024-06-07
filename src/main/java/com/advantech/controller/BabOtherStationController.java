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
import com.advantech.model.db1.BabPreAssyPcsRecord;
import com.advantech.model.db1.BabSettingHistory;
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
import java.util.List;
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
        checkArgument(setting != null, "無投入工單(Can't find processing data.)");
        checkArgument(setting.getBab().getId() == bab_id && setting.getLastUpdateTime() == null, "工單已經關閉(PO is closed.)");
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

            babService.closeBabTrigger(b, bab_id);

            //If not preAssy, refresh endpoint data when user finished the job
            if (b.getIspre() == 0) {
                ep6.syncAndEcho();
            }
        } else {
            babService.stationComplete(b, setting);
        }

        return "success";
    }

    private void checkStation(Bab b, int station) {
        checkArgument(station <= b.getPeople(), "所在站別大於本工單所輸入的人數，請重新確認");
    }

}
