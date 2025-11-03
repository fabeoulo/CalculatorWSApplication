/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi;

import java.time.Duration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

/**
 *
 * @author Justin.Yeh
 */
public abstract class BaseApiClient {

    protected final Duration REQUEST_TIMEOUT = Duration.ofSeconds(3);

    private String webapiUrl;

    public void setWebapiUrl(String webapiUrl) {
        this.webapiUrl = webapiUrl;
    }

    protected WebClient getBufferClient(int mb) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector())
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(mb * 1024 * 1024)) // fix DataBufferLimitException
                .baseUrl(webapiUrl)
                .build();
    }

    protected WebClient getClient() {
        return WebClient.create(webapiUrl);
    }
}
