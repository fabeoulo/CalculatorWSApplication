/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Floor;
import com.advantech.model.PreAssyModuleStandardTime;
import com.advantech.model.PreAssyModuleType;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PreAssyModuleStandardTimeDAO extends AbstractDao<Integer, PreAssyModuleStandardTime> implements BasicDAO_1<PreAssyModuleStandardTime> {

    @Override
    public List<PreAssyModuleStandardTime> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PreAssyModuleStandardTime findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<PreAssyModuleStandardTime> findByModelNameAndFloor(String modelName, Floor f) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("modelName", modelName))
                .add(Restrictions.eq("floor.id", f.getId()))
                .list();
    }

    public List<PreAssyModuleStandardTime> findByModelNameAndPreAssyModuleType(String modelName, PreAssyModuleType type, Floor f) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("modelName", modelName))
                .add(Restrictions.eq("preAssyModuleType.id", type.getId()))
                .add(Restrictions.eq("floor.id", f.getId()))
                .list();
    }

    public List<PreAssyModuleStandardTime> findByFloor(Floor f) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("floor.id", f.getId()))
                .list();
    }

    @Override
    public int insert(PreAssyModuleStandardTime pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PreAssyModuleStandardTime pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PreAssyModuleStandardTime pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}