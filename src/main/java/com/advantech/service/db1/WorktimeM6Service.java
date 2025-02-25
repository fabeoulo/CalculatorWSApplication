/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.WorktimeM6DAO;
import com.advantech.model.db1.WorktimeM6;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WorktimeM6Service {

    @Autowired
    private WorktimeM6DAO dao;

    public List<WorktimeM6> findAll() {
        return dao.findAll();
    }

    public WorktimeM6 findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public WorktimeM6 findByModelName(String modelName) {
        return dao.findByModelName(modelName);
    }

    public List<WorktimeM6> findNotZeroPackingLeadTime() {
        return dao.findNotZeroPackingLeadTime();
    }

    public int insert(WorktimeM6 pojo) {
        return dao.insert(pojo);
    }

    public int insert(List<WorktimeM6> l) {
        return dao.insert(l);
    }

    public int update(WorktimeM6 pojo) {
        return dao.update(pojo);
    }

    public int update(List<WorktimeM6> l) {
        return dao.update(l);
    }

    public int delete(WorktimeM6 pojo) {
        return dao.delete(pojo);
    }

    public int deleteAll() {
        return dao.deleteAll();
    }

}
