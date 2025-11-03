/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi;

import com.advantech.helper.JsonHelper;
import com.advantech.helper.ThreadUtil;
import com.advantech.webapi.model.VlmStation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

/**
 *
 * @author Justin.Yeh
 */
public class VlmApiClient extends BaseApiClient {

    private static final Logger log = LoggerFactory.getLogger(VlmApiClient.class);

    public void sendLoginAsync(VlmStation vlm) {
        String methodName = ThreadUtil.currentMethod();
        String jsonVlm = JsonHelper.getJsonString(vlm);

        super.getClient()
                .post()
                .uri("/start")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(vlm) // auto serialize
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSubscribe(sub -> log.info("Sending {}: {}", methodName, jsonVlm))
                .doOnSuccess(v -> log.info("Successfully sent {}: {}", methodName, jsonVlm))
                .doOnError(e -> log.error("Send {} & data: {} failed： {}", methodName, jsonVlm, e.getMessage()))
                .subscribe();  // fire-and-forget
    }

    public void sendLogoutAsync(VlmStation vlm) {
        String methodName = ThreadUtil.currentMethod();
        String jsonVlm = JsonHelper.getJsonString(vlm);

        super.getClient()
                .post()
                .uri("/stop")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(vlm) // auto serialize
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSubscribe(sub -> log.info("Sending {}: {}", methodName, jsonVlm))
                .doOnSuccess(v -> log.info("Successfully sent {}: {}", methodName, jsonVlm))
                .doOnError(e -> log.error("Send {} & data: {} failed： {}", methodName, jsonVlm, e.getMessage()))
                .subscribe();  // fire-and-forget
    }
}
