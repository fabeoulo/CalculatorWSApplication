/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.facade;

import com.advantech.helper.PropertiesReader;
import com.advantech.model.Test;
import com.advantech.model.TestRecord;
import com.advantech.model.AlarmTestAction;
import com.advantech.model.TestTable;
import com.advantech.service.AlarmTestActionService;
import com.advantech.service.TestService;
import com.advantech.webservice.WebServiceRV;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class TestLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(TestLineTypeFacade.class);

    @Autowired
    private TestService testService;

    @Autowired
    @Qualifier("alarmTestActionService")
    private AlarmTestActionService almService;

    @Autowired
    private PropertiesReader p;
    private Integer maxTestTable;
    private Double TEST_STANDARD_MIN;
    private Double TEST_STANDARD_MAX;
    
    private Map PEOPLE_NOT_MATCH;

    private WebServiceRV rv;

    private final int TEST_USER_NOT_IN_SYSTEM_SIGN = -1, TEST_USER_NOT_IN_XML_SIGN = 2;

    @PostConstruct
    protected void init() {
        log.info(TestLineTypeFacade.class.getName() + " init inner setting and db object.");
        maxTestTable = p.getMaxTestTables();
        TEST_STANDARD_MIN = p.getTestProductivityStandardMin().doubleValue();
        TEST_STANDARD_MAX = p.getTestProductivityStandardMax().doubleValue();
        PEOPLE_NOT_MATCH = new HashMap();
        rv = WebServiceRV.getInstance();
        this.initMap();
//        this.initAlarmSign();
    }

    @Override
    protected void initMap() {
        super.dataMap.clear();
        PEOPLE_NOT_MATCH.clear();

        for (int i = 1; i <= maxTestTable; i++) {
            dataMap.put(("T" + i), NORMAL_SIGN);
        }
    }

    @Override
    protected boolean generateData() {
        boolean isSomeoneUnderStandard = false;
        List<Test> tests = testService.findAll();
        if (hasDataInCollection(tests)) {
            initMap();
            JSONArray userArr = new JSONArray();

            List<TestRecord> kanbanUsers = rv.getTestLineTypeRecords();

            processingJsonObject = new JSONObject();
            boolean isInTheWebService = false;

            for (TestRecord user : kanbanUsers) {

                String jobnumber = user.getUserId();
                String userName = user.getUserName();
                Double productivity = user.getProductivity();

                for (Iterator it = tests.iterator(); it.hasNext();) {
                    Test ti = (Test) it.next();
                    if (ti.getUserId().trim().equals(jobnumber)) {
                        TestTable table = ti.getTestTable();
                        String tableName = table.getName();
                        int status;

                        if (productivity < TEST_STANDARD_MIN && productivity > TEST_STANDARD_MAX) {
                            status = ALARM_SIGN;
                            dataMap.put(tableName, ALARM_SIGN);
                            isSomeoneUnderStandard = true;
                        } else {
                            status = NORMAL_SIGN;
                        }

                        userArr.put(newTestUser(userName, jobnumber, tableName.replace("T", ""), productivity, table.getFloor().getName(), status));
                        it.remove();//把比對過的資料移除，剩下的就是有在本系統XML卻找不到人的使用者
                        isInTheWebService = true;//對到人之後跳出迴圈，換下一個人做比對
                        break;
                    }
                }
                if (isInTheWebService) {
                    isInTheWebService = false;
                } else {
                    PEOPLE_NOT_MATCH.put(userName, TEST_USER_NOT_IN_SYSTEM_SIGN); //沒核對到資料庫的人員傳回m2給前端
                }
            }
            userArr = separateAbnormalUser(tests, userArr);//把剩下的人以異常訊號回報給前端
            processingJsonObject.put("data", userArr);
        } else {
            processingJsonObject = null;
        }
        return isSomeoneUnderStandard;
    }

    private JSONArray separateAbnormalUser(List<Test> l, JSONArray j) {
        String emptyUserName = "n/a";
        Double emptyProductivity = 0.0;
        for (Test ti : l) {
            TestTable table = ti.getTestTable();
            j.put(newTestUser(emptyUserName, ti.getUserId(), table.getName().replace("T", ""), emptyProductivity, table.getFloor().getName(), TEST_USER_NOT_IN_XML_SIGN));
        }
        return j;
    }

    private JSONObject newTestUser(String name, String jobnumber, String tableNo, Double productivity, String sitefloor, int status) {
        return new JSONObject()
                .put("name", name)
                .put("number", jobnumber)
                .put("table", tableNo)
                .put("PRODUCTIVITY", productivity)
                .put("sitefloor", sitefloor)
                .put("isalarm", status);
    }

    @Override
    public void initAlarmSign() {
        List l = almService.findAll();
        almService.delete(l);
        almService.insert(this.mapToAlarmSign(dataMap));
    }

    @Override
    public void setAlarmSign(List l) {
        almService.update(l);
    }

    @Override
    public void resetAlarmSign() {
        almService.reset();
    }

    @Override
    public void setAlarmSignToTestingMode() {
        almService.AlarmToTestingMode();
    }

    public Map getPEOPLE_NOT_MATCH() {
        return PEOPLE_NOT_MATCH;
    }

    @Override
    protected List mapToAlarmSign(Map map) {
        List l = new ArrayList();
        if (map != null && !map.isEmpty()) {
            Iterator it = map.keySet().iterator();
            while (it.hasNext()) {
                Object key = it.next();
                String tableId = key.toString();
                int action = (int) map.get(key);
                l.add(new AlarmTestAction(tableId, action));
            }
        }
        return l;
    }

}