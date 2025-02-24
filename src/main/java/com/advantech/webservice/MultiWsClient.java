/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice;

import com.advantech.webservice.mes.UploadType;
import com.advantech.webservice.mes.WsClient;
import java.io.IOException;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.tempuri.RvResponse;
import org.tempuri.TxResponse;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class MultiWsClient {

    @Autowired
    @Qualifier("wsClient")
    private WsClient m3Client;

    @Autowired
    @Qualifier("wsClient1")
    private WsClient m6Client;

    @Autowired
    @Qualifier("wsClient2")
    private WsClient m9Client;

    public WsClient getClient(final Factory f) {
        switch (f) {
            case TWM3:
                return m3Client;
            case TWM6:
                return m6Client;
            case TWM9:
                return m9Client;
            default:
                return null;
        }
    }

}
