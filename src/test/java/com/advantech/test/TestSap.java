/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.test;

import com.advantech.sap.SapQueryPort;
import com.advantech.sap.SapService;
import com.google.common.base.CharMatcher;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 *
 * @author Justin.Yeh
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestSap {

    @Autowired
    private SapQueryPort port;

    @Autowired
    private SapService sapService;

//    @Test
//    @Transactional
//    @Rollback(true)
    public void testPoMaster() throws Exception {

        JCoFunction function = port.getMaterialInfo("THPI00312ZA", null);

        JCoTable masterTable = function.getTableParameterList().getTable("ZWOMASTER");

        String modelName = masterTable.getString("MATNR").trim();
        modelName = CharMatcher.is('0').trimLeadingFrom(modelName);
    }
    
//    @Test
//    @Transactional
//    @Rollback(true)
    public void testRetrievePoModel() throws Exception {
        String modelName = sapService.retrievePoModel("THPI00312ZA");
    }
}
