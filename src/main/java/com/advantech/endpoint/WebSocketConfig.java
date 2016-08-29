/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.endpoint;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ServiceLoader;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class WebSocketConfig extends Configurator {

    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    
    private Configurator containerDefaultConfigurator;
    
    private static final String ORIGIN = "http://locahost:8080";

    static Configurator fetchContainerDefaultConfigurator() {

        for (Configurator impl : ServiceLoader.load(Configurator.class)) {
            return impl;
        }
        throw new RuntimeException("Cannot load platform configurator");
    }

    Configurator getContainerDefaultConfigurator() {
        if (this.containerDefaultConfigurator == null) {
            this.containerDefaultConfigurator = fetchContainerDefaultConfigurator();
        }
        return this.containerDefaultConfigurator;
    }
    
    @Override
    public void modifyHandshake(ServerEndpointConfig conf,
                                HandshakeRequest req,
                                HandshakeResponse resp) {

        conf.getUserProperties().put("handshakereq", req);
    }

    @Override
    public boolean checkOrigin(String originHeaderValue) {
        // Plug your own algorithm here
        boolean checkStatus = ORIGIN.equals(originHeaderValue);
        log.info("origin check:" + (checkStatus ? "pass": "fail"));
        return checkStatus;
    }
    
    public static void main(String arg0[]){
        try {
            System.out.print(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        }
    }

}