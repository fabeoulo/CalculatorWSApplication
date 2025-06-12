/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.SystemReportDao;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class SystemReportService {

    @Autowired
    private SystemReportDao systemReportDao;

    public List<Map> getCountermeasureForExcel(int lineTypeId, int floorId, String startDate, String endDate, boolean isAboveStandard) {
        return systemReportDao.getCountermeasureForExcel(lineTypeId, floorId, startDate, endDate, isAboveStandard);
    }

    public List<Map> getPersonalAlmForExcel(int lineTypeId, int floorId, String startDate, String endDate, boolean isAboveStandard) {
        return systemReportDao.getPersonalAlmForExcel(lineTypeId, floorId, startDate, endDate, isAboveStandard);
    }

    public List<Map> getCountermeasureAndPersonalAlarmForExcel(String startDate, String endDate) {
        return systemReportDao.getCountermeasureAndPersonalAlarmForExcel(startDate, endDate);
    }

    public List<Map> getEmptyRecordForExcel(int lineTypeId, int floorId, String startDate, String endDate) {
        return systemReportDao.getEmptyRecordForExcel(lineTypeId, floorId, startDate, endDate);
    }

    public List<Map> getCellSuggestionWorkTimeDetailExcel(String startDate, String endDate) {
        return systemReportDao.getCellSuggestionWorkTimeDetailExcel(startDate, endDate);
    }
            
    public List<Map> getTestSuggestionWorkTimeDetailExcel(String startDate, String endDate) {
        return systemReportDao.getTestSuggestionWorkTimeDetailExcel(startDate, endDate);
    }

    public List<Map> getSuggestionWorkTimeDetailExcel(String startDate, String endDate, int lineTypeId) {
        return systemReportDao.getSuggestionWorkTimeDetailExcel(startDate, endDate, lineTypeId);
    }

    public List<Map> getBabPassStationExceptionReportDetails(String po, String modelName, String startDate, String endDate, int lineTypeId) {
        return systemReportDao.getBabPassStationExceptionReportDetails(po, modelName, startDate, endDate, lineTypeId);
    }

    public List<Map> getBabPreAssyDetailForExcel(int lineTypeId, int floorId, String startDate, String endDate) {
        return systemReportDao.getBabPreAssyDetailForExcel(lineTypeId, floorId, startDate, endDate);
    }

    public List<Map> getPreAssyModuleStandardTimeSetting() {
        return systemReportDao.getPreAssyModuleStandardTimeSetting();
    }

    public List<Map> getAssyModelSopStandardTimeSetting() {
        return systemReportDao.getAssyModelSopStandardTimeSetting();
    }

}
