/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.CellStationRecordDAO;
import com.advantech.model.db1.CellStationRecord;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jusitn.Yeh
 */
@Service
@Transactional
public class CellStationRecordService {

    @Autowired
    private CellStationRecordDAO testRecordDAO;

    public List<CellStationRecord> findAll() {
        return testRecordDAO.findAll();
    }

    public CellStationRecord findByPrimaryKey(Object obj_id) {
        return testRecordDAO.findByPrimaryKey(obj_id);
    }

    public List<CellStationRecord> findByDate(DateTime sD, DateTime eD, boolean unReplyOnly) {
        return testRecordDAO.findByDate(sD, eD, unReplyOnly);
    }

    public int insert(CellStationRecord pojo) {
        return testRecordDAO.insert(pojo);
    }

    public int insert(List<CellStationRecord> l) {
        return testRecordDAO.insert(l);
    }

    public int update(CellStationRecord pojo) {
        return testRecordDAO.update(pojo);
    }

    public int delete(CellStationRecord pojo) {
        return testRecordDAO.delete(pojo);
    }

}
