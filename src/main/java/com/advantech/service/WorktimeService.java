/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Worktime;
import java.util.Collection;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WorktimeService {
    
    @Autowired
    private WorktimeDAO worktimeDAO;

    public List<Worktime> findAll() {
        return (List<Worktime>) worktimeDAO.findAll();
    }
    
    public Worktime findByPrimaryKey(Object obj_id) {
        //Initialize the lazy loading relative object
        Worktime worktime = (Worktime) worktimeDAO.findByPrimaryKey(obj_id);
        
        Hibernate.initialize(worktime.getType());
        Hibernate.initialize(worktime.getFloor());
        Hibernate.initialize(worktime.getIdentitBySpeOwnerId());
        Hibernate.initialize(worktime.getIdentitByEeOwnerId());
        Hibernate.initialize(worktime.getIdentitByQcOwnerId());
        
        return worktime;
    }
    
    public List<Worktime> findByPrimaryKeys(Integer... ids) {
        return worktimeDAO.findByPrimaryKeys(ids);
    }
    
    public Worktime findByModel(String modelName) {
        return worktimeDAO.findByModel(modelName);
    }
    
    public int insert(Worktime worktime) {
        return worktimeDAO.insert(worktime);
    }
    
    public int update(Worktime worktime) {
        return worktimeDAO.merge(worktime);
    }
    
    public int delete(List<Worktime> l) {
        for (Worktime m : l) {
            this.delete(m);
        }
        return 1;
    }
    
    public int delete(Object pojo) {
        return worktimeDAO.delete(pojo);
    }
    
}
