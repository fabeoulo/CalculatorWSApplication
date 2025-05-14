/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.PreAssyModuleStandardTimeDAO;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.PreAssyModuleStandardTime;
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
    
    public List<PreAssyModuleStandardTime> findAllWithTypes() {
        return dao.findAllWithTypes();
    }

    public PreAssyModuleStandardTime findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public void checkIsPreAssyModuleTypeExists(PreAssyModuleStandardTime pojo) {
        List<PreAssyModuleStandardTime> l = dao.findByModelNameAndPreAssyModuleType(pojo.getModelName(), pojo.getPreAssyModuleType());
        if (!l.isEmpty()) {
            PreAssyModuleStandardTime existRecord = l.get(0);
            checkArgument(!(pojo.getId() != existRecord.getId() && pojo.getPreAssyModuleType().getId() == existRecord.getPreAssyModuleType().getId()),
                    "PreAssyModuleType is already exist in " + pojo.getModelName());
        }
    }

    public List<PreAssyModuleStandardTime> findByFloor(Floor f) {
        return dao.findByFloor(f);
    }

    public int insertBySeries(String baseModelName, String targetModelName, Floor f) throws CloneNotSupportedException {
        List<PreAssyModuleStandardTime> l = dao.findByModelName(baseModelName);
        checkArgument(!l.isEmpty(), "Can't find data with modelName: " + baseModelName);
        
        for (PreAssyModuleStandardTime p : l) {
            PreAssyModuleStandardTime clone = p.clone();
            clone.setModelName(targetModelName);
            clone.setId(0);
            dao.insert(clone);
        }
        return 1;
    }

    public List<PreAssyModuleStandardTime> findByBab(Bab bab) {
        return dao.findByBab(bab);
    }

    public int insert(PreAssyModuleStandardTime pojo) {
        return dao.insert(pojo);
    }

    public int update(PreAssyModuleStandardTime pojo) {
        return dao.update(pojo);
    }

    public int update(List<PreAssyModuleStandardTime> l) {
        return dao.update(l);
    }

    public int delete(PreAssyModuleStandardTime pojo) {
        return dao.delete(pojo);
    }

}
