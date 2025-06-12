/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.CellStationDAO;
import com.advantech.model.db1.CellStation;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jusitn.Yeh
 */
@Service
@Transactional
public class CellStationService {

    @Autowired
    private CellStationDAO testTableDAO;

    public List<CellStation> findAll() {
        return testTableDAO.findAll();
    }

    public CellStation findByPrimaryKey(Object obj_id) {
        return testTableDAO.findByPrimaryKey(obj_id);
    }

    public List<CellStation> findBySitefloor(String floorName) {
        return testTableDAO.findBySitefloor(floorName);
    }

//    public CellStation findByJobnumber(String jobnumber) {
//        return testTableDAO.findByJobnumber(jobnumber);
//    }

    public int insert(CellStation pojo) {
        return testTableDAO.insert(pojo);
    }

    public int update(CellStation pojo) {
        return testTableDAO.update(pojo);
    }

    public int delete(CellStation pojo) {
        return testTableDAO.delete(pojo);
    }

}
