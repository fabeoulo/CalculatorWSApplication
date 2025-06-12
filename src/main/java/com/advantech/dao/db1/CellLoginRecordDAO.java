/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.CellLoginRecord;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jusitn.Yeh
 */
@Repository
public class CellLoginRecordDAO extends AbstractDao<Integer, CellLoginRecord> implements BasicDAO_1<CellLoginRecord> {

    @Override
    public List<CellLoginRecord> findAll() {
        Criteria c = super.createEntityCriteria();
        c.setFetchMode("cellStation", FetchMode.JOIN);
        c.setFetchMode("cellStation.floor", FetchMode.JOIN);
        return c.list();
    }

    @Override
    public CellLoginRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public CellLoginRecord findByJobnumber(String jobnumber) {
        Criteria c = super.createEntityCriteria();
        c.setFetchMode("cellStation", FetchMode.JOIN);
        c.add(Restrictions.eq("jobnumber", jobnumber));
        return (CellLoginRecord) c.uniqueResult();
    }

    @Override
    public int insert(CellLoginRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(CellLoginRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(CellLoginRecord pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

    public int cleanTests() {
        Query q = super.getSession().createSQLQuery("truncate table {h-schema}CellLoginRecord");
        q.executeUpdate();
        return 1;
    }

}
