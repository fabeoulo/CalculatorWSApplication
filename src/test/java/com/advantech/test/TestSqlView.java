/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.db1.BabPcsDetailHistoryDAO;
import com.advantech.dao.db1.SqlViewDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.view.db1.BabAvg;
import com.advantech.model.view.db3.WorktimeCobots;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import static org.junit.Assert.assertTrue;
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
public class TestSqlView {

    @Autowired
    private SqlViewDAO sqlViewDAO;

    @Autowired
    private BabPcsDetailHistoryDAO babPcsDetailHistoryDAO;

//    @Test
    @Transactional
    @Rollback(true)
    public void testGetBabDetail() {
        DateTime sD = new DateTime(2017, 12, 01, 0, 0, 0, 0);
        DateTime eD = new DateTime(2017, 12, 31, 0, 0, 0, 0);
//        List l = sqlViewDAO.findBabDetail("ASSY", "5", sD, eD, false);
//        assertNotEquals(0, l.size());
//        System.out.println(l.size());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabPcsDetailHistory() {
        List l = babPcsDetailHistoryDAO.findByBabForMap(14223);
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlViewService() {
        List<Map> map = sqlViewDAO.findSensorDIDONames();
        Map<String, String> m2 = map.stream()
                .collect(Collectors.toMap(
                        entry -> (String) entry.getOrDefault("dido_name", ""),
                        entry -> (String) entry.getOrDefault("tagName", ""),
                        (existingValue, newValue) -> existingValue,
                        LinkedHashMap::new
                ));

//        HibernateObjectPrinter.print(m2.toString());
//        List<String> r2 = new ArrayList<>(m2.keySet());

        Set<String> ss = new HashSet<>();
        ss.add("Sensor_71:DI_00");
        ss.add("Sensor_71:DI_01");

        Map<String, String> filterM = m2.entrySet().stream()
                .filter(mm -> ss.contains(mm.getKey()))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        HibernateObjectPrinter.print(filterM.toString());

//        List<Map> result = sqlViewDAO.findProcessBabHasMaxGroup();
//        Map<String, Integer> stationUnclose = new HashMap<>();
//        result.forEach(m -> {
//            String key = (int) m.get("babId") + "-st-" + (int) m.get("station");
//            stationUnclose.put(key, (int) m.get("autoClose"));
//        });
//        HibernateObjectPrinter.print(stationUnclose);
//        int test = stationUnclose.getOrDefault("5", 0);
        String stop = "";

//        List<Map> m = sqlViewDAO.checkSettingHasMaxGroup(190473);
//        Map<String, Integer> stationUncloseM = new HashMap<>();
//        m.forEach(resultM -> {
//            int unClose = (int) resultM.get("autoClose");
//            if (unClose == 1) {
//                stationUncloseM.put((String) resultM.get("tagName"), unClose);
//            }
//        });
//        HibernateObjectPrinter.print(stationUncloseM);
//        int checkResult = stationUncloseM.getOrDefault(5, 0);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testProc() {
        DateTime d = new DateTime();
        String st = "";
        int i = 1;

        sqlViewDAO.findBabAvg(123);
        List<BabAvg> l = sqlViewDAO.findBabAvgInHistory(187987);
        sqlViewDAO.findBabAvgWithBarcode(123);
        sqlViewDAO.findBabLastInputPerLine();
        sqlViewDAO.findBalanceDetail(i);
        sqlViewDAO.findBalanceDetailWithBarcode(i);
        sqlViewDAO.findBarcodeStatus(i);
        sqlViewDAO.findSensorStatus(i);
        sqlViewDAO.findSensorStatusPerStationToday();

//        sqlViewDAO.findWorktime();
//        sqlViewDAO.findWorktime(st);
//        sqlViewDAO.findUserInfoRemote();
//        sqlViewDAO.findUserInfoRemote(st);
    }

    @Autowired
    private com.advantech.service.db3.SqlViewService sqlViewService3;

    @Test
    public void testSqlView3() {
        List<WorktimeCobots> l = sqlViewService3.findCobots(newArrayList(
                "HIT-W121-HSKE",
                "HITW153AP1902-T"));
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l);
    }

}
