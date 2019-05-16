/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.PreAssyModuleStandardTimeDAO;
import com.advantech.model.Floor;
import com.advantech.model.PreAssyModuleStandardTime;
import static com.google.common.base.Preconditions.checkArgument;
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
public class PreAssyModuleStandardTimeService {

    @Autowired
    private PreAssyModuleStandardTimeDAO dao;

    public List<PreAssyModuleStandardTime> findAll() {
        return dao.findAll();
    }

    public PreAssyModuleStandardTime findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public void checkIsPreAssyModuleTypeExists(PreAssyModuleStandardTime pojo, Floor f) {
        List l = dao.findByModelNameAndPreAssyModuleType(pojo.getModelName(), pojo.getPreAssyModuleType(), f);
        checkArgument(l.isEmpty(), "PreAssyModuleType is already exist in " + pojo.getModelName());
    }

    public List<PreAssyModuleStandardTime> findByFloor(Floor f) {
        return dao.findByFloor(f);
    }

    public int insertBySeries(String baseModelName, String targetModelName, Floor f) throws CloneNotSupportedException {
        List<PreAssyModuleStandardTime> l = dao.findByModelNameAndFloor(baseModelName, f);
        checkArgument(l.size() > 0, "Can't find data with modelName: " + baseModelName);
        for (PreAssyModuleStandardTime p : l) {
            PreAssyModuleStandardTime clone = p.clone();
            clone.setModelName(targetModelName);
            clone.setId(0);
            dao.insert(clone);
        }
        return 1;
    }

    public int insert(PreAssyModuleStandardTime pojo) {
        return dao.insert(pojo);
    }

    public int update(PreAssyModuleStandardTime pojo) {
        return dao.update(pojo);
    }

    public int delete(PreAssyModuleStandardTime pojo) {
        return dao.delete(pojo);
    }

}