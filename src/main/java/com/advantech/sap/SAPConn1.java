/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.sap;

import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public class SAPConn1 {

    private static final Logger log = LoggerFactory.getLogger(SAPConn1.class);

    private final String configFileName = "SAPMES";

    private JCoDestination connection;
    private JCoRepository repository;

    /*
      * * 获取SAP连接
      * 
      * @return SAP连接对象
     */
    public JCoDestination getConn() throws JCoException, URISyntaxException {

        if (repository == null) {
            synchronized (SAPConn1.class) {
                File configFile = new File(configFileName + ".jcoDestination");
                if (configFile.exists()) {
                    log.info("找到設定檔 " + configFileName);
                } else {
                    try {
                        log.info("Can't find destinations file in file path " + configFile.getCanonicalPath());
                        createDestinationPropertiesFile(configFileName, getDefaultPropertiesSettings());
                        log.info("Create default destinations setting file success.");
                    } catch (IOException e) {
                        log.error("Can't find destinations file and create new file job fail in path ");
                        log.error(e.getMessage(), e);
                    }
                }

                connection = JCoDestinationManager.getDestination(configFileName);

                repository = connection.getRepository();
            }

        }
        return connection;
    }

    private Properties getDefaultPropertiesSettings() {
        Properties jcoPropeties = new Properties();
        jcoPropeties.setProperty("jco.client.ashost", "172.20.3.6");// 服务器
        jcoPropeties.setProperty("jco.client.sysnr", "06"); // 系统编号
        jcoPropeties.setProperty("jco.client.client", "168"); // SAP集团
        jcoPropeties.setProperty("jco.client.user", "MES.ACL"); // SAP用户名
        jcoPropeties.setProperty("jco.client.passwd", "MESMES"); // 密码
        jcoPropeties.setProperty("jco.client.lang", "ZF"); // 登录语言:ZH EN
        jcoPropeties.setProperty("jco.destination.pool_capacity", "3"); // 最大连接数
        jcoPropeties.setProperty("jco.destination.peak_limit", "10"); // 最大连接线程
        return jcoPropeties;
    }

    private void createDestinationPropertiesFile(String destinationName, Properties connectProperties) {

        String fileName = destinationName + ".jcoDestination";
        File destCfg = new File(fileName);
        try {
            try (FileOutputStream fos = new FileOutputStream(destCfg, false)) {
                connectProperties.store(fos, "sap client test");
            }
            log.info("file: '" + destCfg.getAbsolutePath() + "' written");
        } catch (IOException e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }

}
