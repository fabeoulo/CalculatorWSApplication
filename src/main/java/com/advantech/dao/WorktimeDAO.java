/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.helper.PageInfo;
import com.advantech.model.Worktime;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeDAO extends AbstractDao<Integer, Worktime> implements BasicDAO<Worktime> {

    @Override
    public List<Worktime> findAll() {
        return createEntityCriteria().list();
    }

    public List<Worktime> findAll(PageInfo info) {
        return getByPaginateInfo(info);
    }

    @Override
    public Worktime findByPrimaryKey(Object obj_id) {
        return getByKey((int) obj_id);
    }

    public List<Worktime> findByPrimaryKeys(Integer... id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.in("id", id));
        return criteria.list();
    }

    public Worktime findByModel(String modelName) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("modelName", modelName));
        return (Worktime) criteria.uniqueResult();
    }

    @Override
    public int insert(Worktime pojo) {
        getSession().save(pojo);
        return 1;
    }

    public int merge(Worktime pojo) {
        getSession().merge(pojo);
        return 1;
    }

    public int saveOrUpdate(Worktime pojo) {
        getSession().saveOrUpdate(pojo);
        return 1;
    }

    @Override
    public int update(Worktime pojo) {
        getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(Worktime pojo) {
        getSession().delete(pojo);
        return 1;
    }
}