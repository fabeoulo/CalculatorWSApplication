/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.PreAssyModuleStandardTimeHistory;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PreAssyModuleStandardTimeHistoryDAO extends AbstractDao<Integer, PreAssyModuleStandardTimeHistory> implements BasicDAO_1<PreAssyModuleStandardTimeHistory> {

    @Override
    public List<PreAssyModuleStandardTimeHistory> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PreAssyModuleStandardTimeHistory findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    @Override
    public int insert(PreAssyModuleStandardTimeHistory pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(PreAssyModuleStandardTimeHistory pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PreAssyModuleStandardTimeHistory pojo) {
        super.getSession().delete(pojo);
        return 1;
    }
}
