/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi;

import com.advantech.webapi.model.WaTagNode;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class WaSetTagValue extends WaBaseTagValue {

    private static final Logger log = Logger.getLogger(WaSetTagValue.class.getName());

    private String urlSetTagValue;

    @Override
    protected String getUrl() {
        return urlSetTagValue;
    }

    public void setUrlSetTagValue(String urlSetTagValue) {
        this.urlSetTagValue = urlSetTagValue;
    }

    public void exchange(List<WaTagNode> l) {
        String json = String.format("{\"Tags\":%s}", super.getJsonString(l));
//        log.log(Level.INFO, "SetJsonString======={0}", json);
        String r = postJson(urlSetTagValue, json);
    }
}
