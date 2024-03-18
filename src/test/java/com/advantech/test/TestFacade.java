/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
import com.advantech.facade.FqcLineTypeFacade;
import com.advantech.facade.TestLineTypeFacade;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.service.db1.BabService;
import com.advantech.service.db1.BabSettingHistoryService;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
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
public class TestFacade {

    @Autowired
    private BabLineTypeFacade bF;

    @Autowired
    private TestLineTypeFacade tF;

    @Autowired
    private FqcLineTypeFacade fF;

    @Autowired
    private BabService babService;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

//    @Test
    @Transactional
    @Rollback(false)
    public void testBabLineTypeUnclosedMap() {

        Bab bab = babService.findByPrimaryKey(190043);//189174
//        List<BabSettingHistory> babSettings = babSettingHistoryService.findByBab(bab);
        List<BabSettingHistory> allBabSettings = babSettingHistoryService.findProcessing();
        List<BabSettingHistory> babSettings = allBabSettings.stream()
                .filter(rec -> rec.getBab().getId() == bab.getId()).collect(toList());
//        Map<String, Integer> unclosedMap = bF.getUnclosedMap(bab, babSettings);
    }

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testBabLineTypeFacade() {
        bF.initMap();
        bF.initAlarmSign();
    }

//    @Test
//    @Transactional
//    @Rollback(false)
    public void testTestLineTypeFacade() {
        tF.initMap();
        tF.initAlarmSign();
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFqcLineTypeFacade() {
        fF.generateData();
    }

}
