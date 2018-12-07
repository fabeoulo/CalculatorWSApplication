/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
import com.advantech.facade.FqcLineTypeFacade;
import com.advantech.facade.TestLineTypeFacade;
import com.advantech.helper.CustomPasswordEncoder;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Bab;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.Fqc;
import com.advantech.model.FqcLine;
import com.advantech.model.TagNameComparison;
import com.advantech.model.User;
import com.advantech.quartzJob.HandleUncloseBab;
import com.advantech.service.BabSensorLoginRecordService;
import com.advantech.service.BabService;
import com.advantech.service.BabSettingHistoryService;
import com.advantech.service.FqcLineService;
import com.advantech.service.FqcProductivityHistoryService;
import com.advantech.service.FqcService;
import com.advantech.service.LineBalancingService;
import com.advantech.service.TagNameComparisonService;
import com.advantech.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.mail.MessagingException;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class TestFacade {

    @Autowired
    private BabLineTypeFacade bF;

    @Autowired
    private TestLineTypeFacade tF;

    @Autowired
    private FqcLineTypeFacade fF;

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabLineTypeFacade() {
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testTestLineTypeFacade() {
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testFqcLineTypeFacade() {
        fF.generateData();
    }

}