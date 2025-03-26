/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng Procedure object can't be normally generate by hibernate
 * Result map order disrupted by hibernate
 */
@Repository
public class SystemReportDao extends BasicDAO {

    private Connection getConn() {
        return super.getDBUtilConn(SQL.WebAccess);
    }

    //Bab各工單回復狀況以及詳細
    public List<Map> getCountermeasureForExcel(int lineTypeId, int floorId,
            String startDate, String endDate, boolean isAboveStandard) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_Countermeasure_1(?, ?, ?, ?, ?)}",
                lineTypeId, floorId, startDate, endDate, isAboveStandard);
    }

    //Bab各站別詳細
    public List<Map> getPersonalAlmForExcel(int lineTypeId, int floorId, String startDate,
            String endDate, boolean isAboveStandard) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_PersonalAlarm_1(?, ?, ?, ?, ?)}",
                lineTypeId, floorId, startDate, endDate, isAboveStandard);
    }

    //For效率報表
    public List<Map> getCountermeasureAndPersonalAlarmForExcel(String startDate, String endDate) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_CountermeasureAndPersonalAlarm(?,?)}",
                startDate, endDate);
    }

    //沒有儲存紀錄的工單
    public List<Map> getEmptyRecordForExcel(int lineTypeId, int floorId,
            String startDate, String endDate) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_EmptyRecord_1(?, ?, ?, ?)}",
                lineTypeId, floorId, startDate, endDate);
    }

    //測試工時建議details
    public List<Map> getTestSuggestionWorkTimeDetailExcel(String startDate, String endDate) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_TestPassStationProductivity(?, ?)}",
                startDate, endDate);
    }

    //異常資料details
    public List<Map> getBabPassStationExceptionReportDetails(String po, String modelName,
            String startDate, String endDate, int lineTypeId) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_BabPassStation_ExceptionReport_Details(?, ?, ?, ?, ?)}",
                po, modelName, startDate, endDate, lineTypeId);
    }

    //前置模組資料
    public List<Map> getBabPreAssyDetailForExcel(int lineTypeId, int floorId, String startDate,
            String endDate) {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_BabPreAssyDetail(?,?,?,?)}",
                lineTypeId, floorId, startDate, endDate);
    }

    //前置模組設定
    public List<Map> getPreAssyModuleStandardTimeSetting() {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_PreAssyModuleStandardTimeSetting()}");
    }

    //組裝sop標工設定
    public List<Map> getAssyModelSopStandardTimeSetting() {
        return queryProcForMapList(getConn(),
                "{CALL M3_BW.usp_Excel_AssyModelSopStandardTimeSetting()}");
    }

}
