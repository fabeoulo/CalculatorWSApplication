/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.db1.BabPcsDetailHistoryDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.service.db1.SystemReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.joda.time.DateTime;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSqlReport {

    @Autowired
    private SystemReportService systemReportService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testReports() {
        systemReportService.getCountermeasureForExcel(-1, -1, "2018/01/01", "2018/02/01", true);

        systemReportService.getPersonalAlmForExcel(-1, -1, "2018/01/01", "2018/02/01", true);
        systemReportService.getEmptyRecordForExcel(-1, -1, "2018/01/01", "2018/02/01");
        systemReportService.getBabPassStationExceptionReportDetails("", "", "2018/01/01", "2018/02/01", 1);

        systemReportService.getBabPreAssyDetailForExcel(-1, -1, "2018/01/01", "2018/02/01");

        systemReportService.getPreAssyModuleStandardTimeSetting();
        systemReportService.getAssyModelSopStandardTimeSetting();
    }
    
}
