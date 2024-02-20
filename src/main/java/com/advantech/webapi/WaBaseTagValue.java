/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi;

import com.google.gson.Gson;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Justin.Yeh
 */
public abstract class WaBaseTagValue {

    private static final Logger log = LoggerFactory.getLogger(WaBaseTagValue.class);

    private String username;

    private String password;

    private HttpHeaders headers;

    private final int connectionTimeout = 3000;
    private final int readTimeout = 5000;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //Authorization config
    @PostConstruct
    private void setHeaders() {
        String logonCredentials = username + ":" + password;// id:password
        String base64Credentials = new String(Base64.encodeBase64(logonCredentials.getBytes()));

        HttpHeaders myHeaders = new HttpHeaders();
        myHeaders.add("Authorization", "Basic " + base64Credentials);
        myHeaders.setContentType(MediaType.APPLICATION_JSON);// necessary 
        this.headers = myHeaders;
    }

    public String getJsonString(Object obj) {
        return new Gson().toJson(obj);
    }

    public <C> C jsonToObj(String st, Class<C> clazz) {
        // null if st.isEmpty() 
        return new Gson().fromJson(st, clazz);
    }

    // POST method
    protected String postJson(String url, String json) {
        HttpEntity<String> request = new HttpEntity<>(json, this.headers);
        try {
            ResponseEntity<String> responseEntity = createRestTemplateWithTimeouts()
                    .postForEntity(this.getUrl(), request, String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
    }

    private RestTemplate createRestTemplateWithTimeouts() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
         requestFactory.setConnectTimeout(connectionTimeout);
//        ((HttpComponentsClientHttpRequestFactory) requestFactory).setReadTimeout(readTimeout);
        return new RestTemplate(requestFactory);
    }

    protected abstract String getUrl();
}
