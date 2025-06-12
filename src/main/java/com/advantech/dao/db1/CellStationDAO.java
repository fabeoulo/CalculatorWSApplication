/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.CellStation;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jusitn.Yeh
 */
@Repository
public class CellStationDAO extends AbstractDao<Integer, CellStation> implements BasicDAO_1<CellStation> {

    @Override
    public List<CellStation> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public CellStation findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<CellStation> findBySitefloor(String floorName) {
        Criteria c = super.createEntityCriteria();
        c.createAlias("floor", "f");
        c.createAlias("lineType", "ly"); 
        c.add(Restrictions.eq("f.name", floorName));
        return c.list();
    }

//    public CellStation findByJobnumber(String jobnumber) {
//        Criteria c = super.createEntityCriteria();
//        c.add(Restrictions.eq("jobnumber", jobnumber));
//        return (CellStation) c.uniqueResult();
//    }

    @Override
    public int insert(CellStation pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(CellStation pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(CellStation pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
