/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
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
import com.advantech.webservice.Factory;
import com.advantech.webservice.WebServiceRV;
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
public class TestService {

    @Autowired
    private BabService babService;

    @Autowired
    private LineBalancingService lineBalancingService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    @Autowired
    private BabSensorLoginRecordService babSensorLoginRecordService;

    @Autowired
    private TagNameComparisonService tagNameComparisonService;

    @Autowired
    private UserService userService;

    @Autowired
    private BabLineTypeFacade bf;

    @Autowired
    private CustomPasswordEncoder encoder;

    @Autowired
    private FqcService fqcService;

    @Autowired
    private FqcLineService fqcLineService;

    @Value("${endpoint.quartz.trigger}")
    private String endpointPollingCron;
    
    @Autowired
    private FqcProductivityHistoryService productivityService;

//    @Test
    @Transactional
    @Rollback(true)
    public void testLineBalancingService() throws JobExecutionException {
        new HandleUncloseBab().executeInternal(null);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSettingHistoryService() {

        BabSettingHistory setting2 = babSettingHistoryService.findProcessingByTagName("L8-S-3");
        assertNotNull(setting2);

        assertEquals(setting2.getTagName().getName(), "L8-S-3");
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSensorLoginRecordService() throws JsonProcessingException {

        List l = babSensorLoginRecordService.findByLine(3);
        assertTrue(!l.isEmpty());

        HibernateObjectPrinter.print(l);
    }

//    @Test
    public void testBabSettingHistory() throws JsonProcessingException {

        List<BabSettingHistory> allSettings = babSettingHistoryService.findProcessing();
        Bab b = babService.findByPrimaryKey(12991);

        HibernateObjectPrinter.print(allSettings.get(0));

        List<BabSettingHistory> l = allSettings.stream()
                .filter(rec -> rec.getBab().getId() == b.getId()).collect(toList());

        HibernateObjectPrinter.print(l.get(0));

        assertTrue(!l.isEmpty());

        HibernateObjectPrinter.print(allSettings);
        HibernateObjectPrinter.print(l);
    }

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testHibernateInitialize() {
        TagNameComparison t4 = tagNameComparisonService.findByLampSysTagName("L2-S-4");
        TagNameComparison t7 = tagNameComparisonService.findByLampSysTagName("L2-S-7");
        assertNotNull(t4);
        assertNotNull(t7);

//        babService.checkAndInsert(b, endpointPollingCron, t7);
    }

//    @Test
    public void testBabSaving() {
        try {

            Bab b = babService.findByPrimaryKey(14227);

            lineBalancingService.sendMail(b, 1, 1, 1);

        } catch (MessagingException ex) {

        } catch (Exception ex1) {
            System.out.println(ex1);
        }
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testUserService() {

        int[] ids = {
            33,
            34,
            35,
            36,
            37
        };

        for (int id : ids) {
            User u = userService.findByPrimaryKey(id);
            u.setPassword(encoder.encode(u.getPassword()));
            userService.update(u);
        }
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFqc() {
        Fqc fqc = fqcService.findByPrimaryKey(1);
        assertNotNull(fqc);
        assertEquals(1, fqc.getId());
        assertEquals("PIGB304ZA", fqc.getPo());

        FqcLine fqcLine = fqcLineService.findByPrimaryKey(8);
        assertNotNull(fqcLine);
        assertEquals(8, fqcLine.getId());
        assertEquals("L9", fqcLine.getName());

        assertEquals(fqc.getFqcLine(), fqcLine);
    }
    
//    @Test
    public void testProductivityService(){

    }
    
    @Autowired
    private WebServiceRV rv;
    
    @Test
    @Transactional
    @Rollback(true)
    public void testBarcode(){
        Bab b = babService.findByPrimaryKey(28683);
        String barcode = "TPD0292956";
        String modelName = rv.getPoByBarcode(barcode, Factory.DEFAULT);
        assertEquals(modelName, b.getModelName());
    }

}
