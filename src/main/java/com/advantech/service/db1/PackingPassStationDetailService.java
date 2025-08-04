/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.PackingPassStationDetailDAO;
import com.advantech.model.db1.PackingPassStationDetail;
import java.util.List;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class PackingPassStationDetailService {

    @Autowired
    private PackingPassStationDetailDAO dao;

    public List<PackingPassStationDetail> findAll() {
        return dao.findAll();
    }

    public PackingPassStationDetail findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public List<PackingPassStationDetail> findByDate(DateTime sD, DateTime eD) {
        return dao.findByDate(sD, eD);
    }

    public int insert(PackingPassStationDetail pojo) {
        return dao.insert(pojo);
    }

    public int insert(List<PackingPassStationDetail> l) {
        dao.insert(l);
        return 1;
    }

    public int update(PackingPassStationDetail pojo) {
        return dao.update(pojo);
    }

    public int delete(PackingPassStationDetail pojo) {
        return dao.delete(pojo);
    }

    public int delete(List<PackingPassStationDetail> l) {
        return dao.delete(l);
    }

}
