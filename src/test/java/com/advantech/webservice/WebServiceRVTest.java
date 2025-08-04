/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.db1.PassStationRecord;
import com.advantech.model.db1.TestPassStationDetail;
import com.advantech.model.db1.TestRecord;
import com.advantech.model.db1.UserInfoOnMes;
import com.advantech.model.db1.UserOnMes;
import com.advantech.service.db1.TestRecordService;
import com.advantech.service.db1.TestService;
import com.advantech.webservice.mes.Section;
import com.advantech.webservice.mes.SimpleWebServiceRV;
import com.fasterxml.jackson.core.JsonProcessingException;
import static com.google.common.collect.Lists.newArrayList;
import com.google.gson.Gson;
import static java.lang.System.out;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class WebServiceRVTest {

    @Autowired
    private WebServiceRV rv;

    public WebServiceRVTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getKanbanUsersForString method, of class WebServiceRV.
     */
//    @Test//245
    public void testGetKanbanUsersForString() throws Exception {
        System.out.println("getKanbanUsersForString");
        List<String> result = rv.getKanbanUsersForString(Factory.TWM3);
        assertTrue(!result.isEmpty());
        result = rv.getKanbanUsersForString(Factory.TWM6);
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getKanbanWorkId method, of class WebServiceRV.
     */
//    @Test//245
    public void testGetKanbanWorkId() throws Exception {
        System.out.println("getKanbanWorkId");
        String jobnumber = "A-4960";
        String expResult = "";
        String result = rv.getKanbanWorkId(jobnumber, Factory.TWM3);
        assertNotEquals(expResult, result);
        out.println(result);

        jobnumber = "A-4960";
        expResult = "";
        result = rv.getKanbanWorkId(jobnumber, Factory.TWM6);
        assertNotEquals(expResult, result);
        out.println(result);
    }

    /**
     * Test of getModelnameByPo method, of class WebServiceRV.
     */
//    @Test//245
    public void testGetModelnameByPo() throws Exception {
        System.out.println("getModelnameByPo");
        String po = "PAGB079ZA";
        String expResult = "";
        String result = rv.getModelNameByPo(po, Factory.TWM3);
        assertNotEquals(expResult, result);
        out.println(result);

        po = "TGN000141ZA";
        expResult = "";
        result = rv.getModelNameByPo(po, Factory.TWM6);
        assertNotEquals(expResult, result);
        out.println(result);
    }

//    @Autowired
//    private MultiWsClient mClient;
//    @Test
//    public void testWsRvJar() {
//        SimpleWebServiceRV rv = new SimpleWebServiceRV();
//        Factory f = Factory.TWM3;
//        rv.setWsClient(mClient.getClient(f));
//        String jobnumber = "A-5131";
//        String queryString = "<root><METHOD ID='Advantech.IMG.SYS.BLL.QryLogion'/><USER_INFO><USER_NO>"
//                + jobnumber
//                + "</USER_NO><PASSWORD></PASSWORD><STATUS>A</STATUS></USER_INFO>"
//                + "<EXT_DEPT>" + f.token() + "</EXT_DEPT></root>";
//        List<Object> lObjects = rv.getWebServiceData(queryString);
//        Document doc = rv.getWebServiceDataForDocument(queryString);
//        HibernateObjectPrinter.print(lObjects);
//        HibernateObjectPrinter.print(doc);
//        Node node = doc.getFirstChild();
//        Node mainMessageNode = node.getFirstChild();
//        HibernateObjectPrinter.print(node);
//        HibernateObjectPrinter.print(mainMessageNode);
//    }
    /**
     * Test of getMESUser method, of class WebServiceRV.
     */
    @Test//245
    public void testGetMESUser() {
        System.out.println("getMESUser");
        String jobnumber = "A-7275";
        UserOnMes expResult = null;
        UserOnMes result = rv.getMESUser(jobnumber, Factory.TWM3);
        assertNotEquals(expResult, result);
        out.println(new Gson().toJson(result));

        jobnumber = "A-9043";
        expResult = null;
        result = rv.getMESUser(jobnumber, Factory.TWM6);
        assertNotEquals(expResult, result);
        out.println(new Gson().toJson(result));
    }

    /**
     * Test of getPassStationRecords method, of class WebServiceRV.
     */
//    @Test//245
    public void testGetPassStationRecords() {
        System.out.println("getPassStationRecords");
        String po = "THL007939ZA";
        List<PassStationRecord> result = rv.getPassStationRecords(po, 16, Factory.TWM3);
        assertTrue(!result.isEmpty());
        HibernateObjectPrinter.print(result);

        po = "TAN000010ZA";
        result = rv.getPassStationRecords(po, 162, Factory.TWM6);
        assertTrue(!result.isEmpty());
        HibernateObjectPrinter.print(result);
    }

    /**
     * Test of getTestLineTypeUsers method, of class WebServiceRV.
     */
//    @Test//245
    public void testGetTestLineTypeUsers() {
        System.out.println("getTestLineTypeUsers");
        List<TestRecord> expResult = null;
        List<TestRecord> result = rv.getTestLineTypeRecords(Factory.TWM3);
        assertNotEquals(expResult, result);
        for (TestRecord t : result) {
            out.println(new Gson().toJson(t));
        }

        expResult = null;
        result = rv.getTestLineTypeRecords(Factory.TWM6);
        assertNotEquals(expResult, result);
        for (TestRecord t : result) {
            out.println(new Gson().toJson(t));
        }
    }

//    @Test//245 l size0
    public void testGetTestLineTypeRecord() throws JsonProcessingException {
        System.out.println("getTestLineTypeRecord");
        List<TestRecord> l = rv.getTestLineTypeRecords(Factory.TWM3);
        assertNotNull(l);
        HibernateObjectPrinter.print(l);

        l = rv.getTestLineTypeRecords(Factory.TWM6);
        assertNotNull(l);
        HibernateObjectPrinter.print(l);
    }

//    @Test//245
    public void testGetModelNameByBarcode() throws JsonProcessingException {
        String value = rv.getPoByBarcode("TPAD555444", Factory.TWM3);
        assertEquals(value, "TPN000181ZA");
        value = rv.getPoByBarcode("IDA0555653", Factory.TWM6);
        assertEquals(value, "TAN000010ZA");
    }

//    @Test//245
    public void testGetMesPassCountRecords() {
        DateTime eD = new DateTime("2023-03-29");
        DateTime sD = eD.minusDays(1);
        List l = rv.getMesPassCountRecords(sD, eD, Factory.TWM3);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l);

        l = rv.getMesPassCountRecords(sD, eD, Factory.TWM6);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l);
    }

    @Autowired
    private TestService testService;

    @Autowired
    private TestRecordService testRecordService;

//    @Test//245
    public void testGetTestPassStationDetails() {
        DateTime today = new DateTime("2025-06-19");

        int hr = today.getHourOfDay() >= 20 ? 20 : 8;
        DateTime eD = new DateTime(today).withTime(hr, 30, 0, 0);
        DateTime sD = eD.minusHours(12);//.minusDays(today.getDayOfWeek() == 1 && hr == 8 ? 2 : 0);

        List<String> users = newArrayList("'A-10945'");

//        List<TestRecord> records = testRecordService.findByDate(sD, eD, false);
//        List<String> jobnumbers = records.stream().map(t -> "'" + t.getUserId() + "'").distinct().collect(Collectors.toList());
        List<Integer> stations = newArrayList(3, 11, 30, 151);

        stations.forEach(s -> {
            Section section = (s == 3 ? Section.BAB : Section.TEST);
            List<TestPassStationDetail> l = rv.getTestPassStationDetails(users, section, s, sD, eD, Factory.TWM3);
            //assertTrue(!l.isEmpty());
            HibernateObjectPrinter.print(l);

            l = rv.getTestPassStationDetails(users, section, s, sD, eD, Factory.TWM6);
            HibernateObjectPrinter.print(l);
        });
    }

//    @Test//245
    public void testGetUsersInfoOnMes() {
        System.out.println("getUsersInfoOnMes");
        List l = rv.getUsersInfoOnMes(Factory.TWM3);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l.get(0));

        l = rv.getUsersInfoOnMes(Factory.TWM6);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l.get(0));

        List<UserInfoOnMes> remoteDirectUser = ((List<UserInfoOnMes>) l).stream()
                .filter(ur -> (ur.getUnitNo() != null && ur.getUserNo().equals("A-8887")))
                .collect(toList());
    }

//    @Test//245
    public void testRptStationQtys() {
        DateTime sD = new DateTime().withTime(0, 0, 0, 0);
        DateTime eD = new DateTime().plusDays(1).withTime(0, 0, 0, 0);

        List l = rv.getRptStationQtys("EKI-1524I-CE", 2, Factory.TWM3);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l.get(0));

        l = rv.getRptStationQtys("IDK-1110WP-50XGA1E", 2, Factory.TWM6);
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l.get(0));
    }
}
