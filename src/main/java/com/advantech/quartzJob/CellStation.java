/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.entity.Cell;
import com.advantech.entity.PassStation;
import com.advantech.helper.CronTrigMod;
import com.advantech.service.BasicService;
import com.advantech.service.CellService;
import com.advantech.webservice.WebServiceRV;
import static java.lang.System.out;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

/**
 *
 * @author Wei.Cheng
 */
public class CellStation implements Job {

    private String currentPO;
    private Integer currentLineId;
    private Integer currentApsLineId;
    private JobKey currentJobKey;
    private TriggerKey currentTriggerKey;
    private String today;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        JobDataMap dataMap = jec.getJobDetail().getJobDataMap();
        this.currentPO = (String) dataMap.get("PO");
        this.currentLineId = (Integer) dataMap.get("lineId");
        this.currentApsLineId = (Integer) dataMap.get("apsLineId");
        this.currentTriggerKey = jec.getTrigger().getKey();
        this.currentJobKey = jec.getJobDetail().getKey();
        this.today = (String) dataMap.get("today");
        syncMesDataToDatabase();
    }

    private void syncMesDataToDatabase() {
        //先看紀錄幾筆了
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(currentPO, currentApsLineId);

        //確認已經開始了
        if (!l.isEmpty()) {
            //get PO quantity view 得到該工單所要做的機台數，超過Job self unsched.(台數 * 2 == 紀錄)
            //未到達台數持續Polling database find new data.
            checkDifferenceAndInsert(currentPO, currentApsLineId);
//            if (isPieceReachMaxium(l)) {
//                jobSelfRemove();
//            }
        }
    }

    public static void checkDifferenceAndInsert(String PO, int apsLineId) {

        out.println("Begin check");
        List<PassStation> l = WebServiceRV.getInstance().getPassStationRecords(PO, apsLineId);
        List<PassStation> history = BasicService.getPassStationService().getPassStation(PO);
        List<PassStation> newData = (List<PassStation>) CollectionUtils.subtract(l, history);

        if (!newData.isEmpty()) {
            out.println("Begin insert");
            BasicService.getPassStationService().insertPassStation(newData);
        } else {
            out.println("No difference");
        }
    }

    private boolean isPieceReachMaxium(List<PassStation> l) {
        int totalPiece = BasicService.getBabService().getPoTotalQuantity(currentPO);
        return (l.size() / 2) == totalPiece;
    }

    private void jobSelfRemove() {
        CronTrigMod ctm = CronTrigMod.getInstance();
        try {
            ctm.removeJob(currentJobKey);
            CellService cellService = BasicService.getCellService();
            List<Cell> list = cellService.getCellProcessing(currentLineId);
            if (!list.isEmpty()) {
                cellService.deleteCell((Cell) list.get(0));
            }
        } catch (SchedulerException ex) {
            out.println(ex.toString());
        }
    }
}