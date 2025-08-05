/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.WorktimeExtrasDAO;
import com.advantech.model.db1.WorktimeExtras;
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
public class WorktimeExtrasService {

    @Autowired
    private WorktimeExtrasDAO dao;

    public List<WorktimeExtras> findAll() {
        return dao.findAll();
    }

    public WorktimeExtras findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public WorktimeExtras findByModelName(String modelName) {
        return dao.findByModelName(modelName);
    }

    public List<WorktimeExtras> findNotZeroPackingLeadTime() {
        return dao.findNotZeroPackingLeadTime();
    }

    public int insert(WorktimeExtras pojo) {
        return dao.insert(pojo);
    }

    public int insert(List<WorktimeExtras> l) {
        return dao.insert(l);
    }

    public int update(WorktimeExtras pojo) {
        return dao.update(pojo);
    }

    public int update(List<WorktimeExtras> l) {
        return dao.update(l);
    }

    public int delete(WorktimeExtras pojo) {
        return dao.delete(pojo);
    }

    public int deleteAll() {
        return dao.deleteAll();
    }

}
