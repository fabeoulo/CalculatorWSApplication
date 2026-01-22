/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng How to inject variable to static field
 * https://www.mkyong.com/spring/spring-inject-a-value-into-static-variables/
 */
@Component
public class HibernateBatchUtils {

    private static Integer batchSize;

    @Value("${HIBERNATE.JDBC.BATCHSIZE}")
    public void setBatchSize(Integer b) {
        batchSize = b;
    }

    public static void flushIfReachFetchSize(Session session, int currentRow) {
        if (currentRow % batchSize == 0 && currentRow > 0) {
            session.flush();
            session.clear();
        }
    }

}
