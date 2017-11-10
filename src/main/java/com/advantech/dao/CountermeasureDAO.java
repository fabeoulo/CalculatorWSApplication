/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Countermeasure;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng bab資料表就是生產工單資料表
 */
@Repository
public class CountermeasureDAO extends BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(CountermeasureDAO.class);

    @Autowired
    private ActionCodeDAO actionCodeDAO;

    private Connection getConn() {
        return getDBUtilConn(SQL.WebAccess);
    }

    private List<Countermeasure> queryCountermeasureTable(String sql, Object... params) {
        return queryForBeanList(getConn(), Countermeasure.class, sql, params);
    }

    public List<Countermeasure> getCountermeasures() {
        return this.queryCountermeasureTable("SELECT * FROM Countermeasure");
    }

    public List<Map> getCountermeasuress() {
        return queryForMapList(getConn(), "SELECT * FROM Countermeasure");
    }

    public Countermeasure getCountermeasure(int BABid) {
        List l = this.queryCountermeasureTable("SELECT * FROM Countermeasure WHERE BABid = ?", BABid);
        return l.isEmpty() ? null : (Countermeasure) l.get(0);
    }

    public List<Map> getCountermeasureView() {
        return queryForMapList(this.getConn(), "SELECT * FROM CountermeasureView ORDER BY 1");
    }

    public List<Map> getCountermeasureView(int BABid) {
        return queryForMapList(this.getConn(), "SELECT * FROM CountermeasureView WHERE id = ?", BABid);
    }

    public List<Map> getUnFillCountermeasureBabs() {
        return queryForMapList(this.getConn(), "SELECT * FROM unFillCountermeasureView ORDER BY btime DESC");
    }

    public List<Map> getUnFillCountermeasureBabs(String sitefloor) {
        return queryForMapList(this.getConn(), "SELECT * FROM unFillCountermeasureView WHERE sitefloor = ? ORDER BY btime DESC", sitefloor);
    }

    public List<Map> getCountermeasureForExcel(String startDate, String endDate) {
        return queryProcForMapList(this.getConn(), "{CALL countermeasureDownExcel(?,?)}", startDate, endDate);
    }

    public List<Map> getPersonalAlmForExcel(String startDate, String endDate) {
        return queryProcForMapList(this.getConn(), "{CALL personalAlmDownExcel_1(?,?)}", startDate, endDate);
    }

    public List<Map> getCountermeasureAndPersonalAlm(String startDate, String endDate) {
        return queryProcForMapList(this.getConn(), "{CALL countermeasureAndPersonalAlmDownExcel(?,?)}", startDate, endDate);
    }

    public List<Map> getEditor(int cm_id) {
        return queryForMapList(getConn(), "select editor from countermeasureEditorView WHERE cm_id = ?", cm_id);
    }

    public boolean insertCountermeasure(int BABid, String solution, List<String> actionCodes, String editor) {
        try {
            Connection conn = this.getConn();
            QueryRunner qRunner = new QueryRunner();

            Object[] param3 = {BABid, solution};
            int cm_id = qRunner.insert(conn, "INSERT INTO Countermeasure(BABid, solution) values(?,?)", new ScalarHandler<BigDecimal>(), param3).intValue();//關閉線別

            actionCodeDAO.insert(cm_id, actionCodes);
            insertEvent(cm_id, editor, "insert");

            return true;
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private boolean insertEvent(int cm_id, String editor, String event) {
        return update(getConn(), "INSERT INTO CountermeasureEvent(cm_id, editor, event) VALUES(?,?,?)", cm_id, editor, event);
    }

    public boolean updateCountermeasure(int BABid, String solution, List<String> actionCodes, String editor) {
        Countermeasure cm = this.getCountermeasure(BABid);
        int cm_id = cm.getId();
        updateSolution(solution, cm_id);
        actionCodeDAO.delete(cm_id);
        actionCodeDAO.insert(cm_id, actionCodes);
        insertEvent(cm_id, editor, "update");
        return true;
    }

    private boolean updateSolution(String solution, int cm_id) {
        return update(getConn(), "UPDATE Countermeasure SET solution = ? WHERE id = ?", solution, cm_id);
    }

    public boolean deleteCountermeasure(int id) {
        return updateCountermeasure("DELETE FROM Countermeasure WHERE BABid = ?", id);
    }

    private boolean updateCountermeasure(String sql, Object... params) {
        return update(getConn(), sql, params);
    }
}