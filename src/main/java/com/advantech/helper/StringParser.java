/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class StringParser {

    private static final Logger log = LoggerFactory.getLogger(StringParser.class);

    public static int strToInt(String str) {
        return ((str == null || "".equals(str)) ? 0 : Integer.parseInt(str));
    }

    public static Double strToDouble(String str) {
        return ((str == null || "".equals(str)) ? 0.0 : Double.parseDouble(str));
    }

    public static String clobToString(Clob data) {
        StringBuilder sb = new StringBuilder();
        try {
            Reader reader = data.getCharacterStream();
            BufferedReader br = new BufferedReader(reader);

            String line;
            while (null != (line = br.readLine())) {
                sb.append(line);
            }
            br.close();
        } catch (SQLException | IOException e) {
            log.error(e.toString());
        }
        return sb.toString();
    }

    public static String defaultStringIfNull(Object obj, String defaultVal) {
        return ObjectUtils.defaultIfNull(obj, defaultVal).toString().trim();
    }
}
