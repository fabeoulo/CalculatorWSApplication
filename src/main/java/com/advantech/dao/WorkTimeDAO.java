/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorkTimeDAO extends BasicDAO {

    private Connection getConn() {
        return getDBUtilConn(BasicDAO.SQL.WebAccess);
    }

    //抓取測試工時
    public List<Map> getWorkTimePerModelView(String modelName) {
        return queryForMapList(getConn(), "SELECT * FROM workTimePerModelView WHERE Model_name = ?", modelName);
    }
}