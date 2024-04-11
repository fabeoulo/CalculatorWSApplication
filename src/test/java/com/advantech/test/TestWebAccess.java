/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.test;

import com.advantech.facade.BabLineTypeFacade;
import com.advantech.facade.TestLineTypeFacade;
import com.advantech.model.db1.AlarmBabAction;
import com.advantech.model.db1.AlarmDO;
import com.advantech.model.db1.AlarmTestAction;
import com.advantech.service.db1.AlarmBabActionService;
import com.advantech.service.db1.AlarmDOService;
import com.advantech.service.db1.AlarmTestActionService;
import com.advantech.service.db1.SqlViewService;
import com.advantech.webapi.WaSetTagValue;
import com.advantech.webapi.WaGetTagValue;
import com.advantech.webapi.model.WaTagNode;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import static org.junit.Assert.*;
import org.quartz.JobExecutionException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Justin.Yeh
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestWebAccess {

    @Autowired
    private AlarmDOService alarmDOService;

    @Autowired
    private WaSetTagValue waSetTagValue;

    @Autowired
    private WaGetTagValue waGetTagValue;

    @Autowired
    private BabLineTypeFacade bF;

    @Autowired
    private TestLineTypeFacade tF;

    @Autowired
    private AlarmTestActionService alarmTestService;

    @Autowired
    private AlarmBabActionService alarmBabService;

//    @Test
//    @Transactional
//    @Rollback(true)
    public void testBabLineTypeFacade() {
//        String st = "";
//        try {
//            AlarmDO rs = alarmDOService.findByPrimaryKey("LD-L-8");// key LD-L-8 one to many
//            st = rs.getCorrespondDO();
//            System.out.println("AlarmDO is : " + st);
//        } catch (Exception ex) {
//            throw ex;
////            System.out.println("Exception is : " + ex);
//        }
        System.out.println("testBabLineTypeFacade= ");

        List<AlarmBabAction> alarmBabs = Arrays.asList(
                new AlarmBabAction("T24", 1),
                new AlarmBabAction("L3-L-6", 1));
//        alarmBabs = babService.findAll();
        bF.setAlarmSign(alarmBabs);
        bF.resetAlarmSign();

//        List<AlarmTestAction> alarmActions = alarmTestService.findAll();
//        List<AlarmTestAction> alarmActions = Arrays.asList(
//                new AlarmTestAction("T24", 1));
//        tF.setAlarmSign(alarmActions);
//        tF.resetAlarmSign();
//        bF.initMap();
//        bF.initAlarmSign();
//        List l = bF.mapToAlarmSign(bF.getMap());
//        bF.setAlarmSign(l);
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testAlarmDOService() {
        List<AlarmBabAction> alarmBabs = alarmBabService.findAll();
        //find table ID
        List<String> tableIds = alarmBabs.stream()
                .map(bab -> bab.getTableId())
                .collect(Collectors.toList());

        //find active DO
        Map allActiveTags = waGetTagValue.getMap();
        List<String> liveDOs = new ArrayList<>(allActiveTags.keySet());

        List<AlarmDO> f_alarmDOs = alarmDOService.findAllByTablesAndDOs(tableIds, liveDOs);
//        List<AlarmDO> f_alarmDOs2 = alarmDOService.findDOByTables(tableIds);
    }

//    @Test
    public void getTagValue() {

        System.out.println("map.size:= " + waGetTagValue.getMap().size());
        waGetTagValue.updateActiveDOs();
        System.out.println("map.size:= " + waGetTagValue.getMap().size());
    }

    @Autowired
    private SqlViewService sqlViewService;

//    @Test
    public void testCheckTagNode() {
        List<Map> _DIDONamesMaps = sqlViewService.findSensorDIDONames();
        String key = sqlViewService.getVwDiDoColumn();
        List<String> allDIDO = _DIDONamesMaps.stream()
                .map(m -> (String) m.getOrDefault(key, ""))
                .collect(Collectors.toList());
        Map<String, Integer> map = waGetTagValue.getMapByTagNames(allDIDO);

        Map<String, Integer> mapDO = map.entrySet().parallelStream()
                .filter(e -> e.getKey().contains("DO"))
                .collect(Collectors.toConcurrentMap(e -> e.getKey(), e -> e.getValue()));
        waGetTagValue.setMap(mapDO);

        Set<String> currentNodes = new HashSet<>(map.keySet());
        List<String> copyDO = new ArrayList<>(allDIDO);
        List<String> currentNodesTest = new ArrayList<>(currentNodes);
        currentNodesTest.sort(null);
        boolean b = copyDO.retainAll(currentNodes);
    }

    /* POST */
    public void setTagValueSubList(String param) {

        Objects.requireNonNull(param);

//        int arrLen = 10;
//        List<List<WaTagNode>> subL = Lists.partition(l, arrLen);
//        subL.forEach(c -> {
//            waSetTagValue.exchange(c);
//        });
//        System.out.println("getJsonString" + waSetTagValue.getJsonString());
    }

//    @Test
    public void setTagValue() {
        List<AlarmBabAction> alarmBabs = new ArrayList<>();
        alarmBabs.add(new AlarmBabAction("L4-L-1", 0));
        alarmBabs.add(new AlarmBabAction("L3-L-2", 0));

//        waGetTagValue.updateActiveDOs();
        bF.setAlarmSign(alarmBabs);

        if (alarmBabs != null) {
//            List<AlarmDO> listDO = findDOByTables(alarmBabs);
//            Map<String, String> mapTablesDOs = listDO.stream()
//                    .collect(Collectors.toMap(AlarmDO::getProcessName, AlarmDO::getCorrespondDO));
//            System.out.println("mapTablesDOs.size=====" + mapTablesDOs.size());
//
//            List<WaTagNode> requestModels = new ArrayList<>();
//            alarmBabs.forEach(e -> {
////            alarmBabs.stream().filter(e -> {
//                if (mapTablesDOs.containsKey(e.getTableId())) {
//                    requestModels.add(
//                            new WaTagNode(mapTablesDOs.get(e.getTableId()), e.getAlarm())
//                    );
//                } 
//            });
//            System.out.println("requestModels.size=====" + requestModels.size());
//
//            //filter
//            Map tagNodes = WaGetTagValue.getMap();
//            requestModels.stream().peek(e -> {
//                tagNodes.containsKey(e.getName());
//            }).collect(Collectors.toList());
//            System.out.println("requestModels.size=====" + requestModels.size());
//        exchange(l);
//        System.out.println("getJsonString" + rb.getJsonString());
        }
    }

    @Test
    public void testGetJsonString() {
        String[] testValues = {"a", "b", "c", "d"};
        String result = waGetTagValue.getJsonString(Arrays.asList(testValues));
        System.out.println(result);
    }
}
