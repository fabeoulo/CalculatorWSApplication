/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.CellPassStationDetailDAO;
import com.advantech.model.db1.CellPassStationDetail;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Jusitn.Yeh
 */
@Service
@Transactional
public class CellPassStationDetailService {

    @Autowired
    private CellPassStationDetailDAO dao;

    public List<CellPassStationDetail> findAll() {
        return dao.findAll();
    }

    public CellPassStationDetail findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public List<CellPassStationDetail> findByDate(DateTime sD, DateTime eD) {
        return dao.findByDate(sD, eD);
    }

    public int insert(CellPassStationDetail pojo) {
        return dao.insert(pojo);
    }

    public int insert(List<CellPassStationDetail> l) {
        dao.insert(l);
        return 1;
    }

    public int update(CellPassStationDetail pojo) {
        return dao.update(pojo);
    }

    public int delete(CellPassStationDetail pojo) {
        return dao.delete(pojo);
    }

    public int delete(List<CellPassStationDetail> l) {
        return dao.delete(l);
    }

}
