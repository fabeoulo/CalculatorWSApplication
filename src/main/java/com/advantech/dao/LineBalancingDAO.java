/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Bab;
import com.advantech.model.LineBalancing;
import java.util.List;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class LineBalancingDAO extends AbstractDao_1<Integer, LineBalancing> implements BasicDAO_1<LineBalancing> {

    private static final Logger log = LoggerFactory.getLogger(LineBalancingDAO.class);

    @Override
    public List<LineBalancing> findAll() {
        Query q = super.getSession().createSQLQuery("SELECT * FROM Line_Balancing_Main_F");
        return q.list();
    }

    @Override
    public LineBalancing findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public LineBalancing getMaxBalance(Bab bab) {
        Query q = super.getSession().createQuery(
                "FROM LineBalancing where modelName = :modelName "
                        + "and people = :people and lineType = :lineType "
                        + "and balance is not null "
                        + "and (avg1 is not null or avg1 != 0)");
        q.setParameter("modelName", bab.getModel_name());
        q.setParameter("people", bab.getPeople());
        q.setParameter("lineType", bab.getLinetype());
        return (LineBalancing) q.uniqueResult();
    }

    @Override
    public int insert(LineBalancing pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(LineBalancing pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(LineBalancing pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}