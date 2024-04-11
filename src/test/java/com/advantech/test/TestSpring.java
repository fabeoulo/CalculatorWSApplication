/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.db1.BabDataCollectMode;
import com.advantech.model.view.db1.BabAvg;
import com.advantech.service.db2.LineBalancingService;
import com.advantech.service.db1.SqlViewService;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSpring {

    @Autowired
    private SqlViewService sqlViewService;

    @Autowired
    private LineBalancingService lineBalancingService;

    private final double DELTA = 1e-15;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${test.productivity.bypass.hours: 0}")
    private Integer[] testByPassHours;

    @Autowired
    private PropertiesReader reader;

//    @Test
//    @Transactional
//    @Rollback(true)
    public void testSqlViewService() {
        List<Map> l = sqlViewService.findSensorDIDONames();
    }
    
//    @Test
    public void testLineBalanceCount() {
        List<BabAvg> l = sqlViewService.findBabAvgInHistory(13220);
//        assertEquals(3, l.size());

        double max = l.stream().mapToDouble(i -> i.getAverage()).max().getAsDouble();

//        assertEquals(830.667, max, DELTA);
        double sum = l.stream().mapToDouble(i -> i.getAverage()).sum();

        System.out.printf("sum: %.3f and max: %.3f \n", sum, max);

        double balance = lineBalancingService.caculateLineBalance(l);

        System.out.printf("balance: %.3f \n", balance);
    }

//    @Test
    public void testContextParam() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("Local Host: " + localhost.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        assertTrue(testByPassHours == null);
        System.out.println(Arrays.toString(this.testByPassHours));
    }

//    @Test
    public void testPropertiesReader() {
        BabDataCollectMode mode = reader.getBabDataCollectMode();
        assertEquals(BabDataCollectMode.MANUAL, mode);
    }

    private HttpHeaders getHeaders() {
        String plainCredentials = "admin:";
        String base64Credentials = new String(Base64.encodeBase64(plainCredentials.getBytes()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

//    @Test
    public void testRESTful() throws UnsupportedEncodingException {
        String url = "http://172.20.128.235/WaWebService/Json/SetTagValue/DongHuSystem";

        JSONObject requestJson = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        jsonArr.put(new JSONObject().put("Name", "Sensor_153:DO_02").put("Value", "0"));
        requestJson.put("Tags", jsonArr);

        System.out.println(requestJson.toString());

        HttpEntity<String> request = new HttpEntity<String>(requestJson.toString(), getHeaders());

        String answer = restTemplate.postForObject(url, request, String.class);
        System.out.println(answer);
    }

//    @Test
    public void testWebAccessRESTful() {

        int value = 0;

        JSONObject requestJson = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        jsonArr.put(new JSONObject().put("Name", "Sensor_153:DO_00").put("Value", value));
        jsonArr.put(new JSONObject().put("Name", "Sensor_153:DO_01").put("Value", value));
        jsonArr.put(new JSONObject().put("Name", "Sensor_153:DO_02").put("Value", value));
        requestJson.put("Tags", jsonArr);

        HibernateObjectPrinter.print(requestJson.toString());
        HashMap<String, Object> yourHashMap = new Gson().fromJson(requestJson.toString(), HashMap.class);

        HibernateObjectPrinter.print(yourHashMap);
        ((List) yourHashMap.get("Tags")).forEach(map -> {
            HibernateObjectPrinter.print(map);
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) map).entrySet()) {
                String key = entry.getKey();
                if (entry.getValue() instanceof Double) {
                    int valueO = ((Double) entry.getValue()).intValue();
                    entry.setValue(valueO);
                }
            }
        });
        HibernateObjectPrinter.print(yourHashMap);
    }

}
