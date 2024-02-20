/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi;

import com.advantech.webapi.model.WaTagNode;
import com.advantech.webapi.model.WaGetTagResponseModel;
import com.advantech.service.db1.AlarmDOService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class WaGetTagValue extends WaBaseTagValue {

    private static final Logger log = LoggerFactory.getLogger(WaGetTagValue.class);

    private String urlGetTagValue;

    private Map<String, Integer> map = new HashMap<>();

    @Autowired
    private AlarmDOService alarmDOService;

    // quartz everyday in BabLineTypeFacade(BasicLineTypeFacade) in DataBaseInit
    @PostConstruct
    public void updateActiveDOs() {
        List<String> allTagNames = alarmDOService.findAllDistinctCorrespondDO();
        map = getMapByTagNames(allTagNames);
    }

    @Override
    protected String getUrl() {
        return urlGetTagValue;
    }

    public void setUrlGetTagValue(String urlGetTagValue) {
        this.urlGetTagValue = urlGetTagValue;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public Map<String, Integer> getMapByTagNames(List<String> tagNames) {
        String json = getJsonString(tagNames);
        return getMapByTag(getResponseBodys(json));
    }

    private WaGetTagResponseModel getResponseBodys(String json) {
        String jsonResponse = super.postJson(urlGetTagValue, json);
        return super.jsonToObj(jsonResponse, WaGetTagResponseModel.class);
    }

    private Map<String, Integer> getMapByTag(WaGetTagResponseModel responseBodys) {
        Map<String, Integer> tempMap = new HashMap<>();
        if (responseBodys != null) {
            tempMap = responseBodys.getValues().stream()
                    .filter(f -> (f.getValue() == 0 || f.getValue() == 1))
                    .collect(Collectors.toMap(WaTagNode::getName, WaTagNode::getValue));
        }
//        log.info("map.size() : " + tempMap.size());
        return tempMap;
    }

    private String getJsonString(List<String> l) {
        JSONObject result = new JSONObject();
        JSONArray tags = new JSONArray();

        for (String s : l) {
            JSONObject tag = new JSONObject();
            tag.put("Name", s);
            tags.put(tag);
        }

        result.put("Tags", tags);
        return result.toString();
    }
}
