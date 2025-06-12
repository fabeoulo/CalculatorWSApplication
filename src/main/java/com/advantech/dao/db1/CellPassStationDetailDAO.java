/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.CellPassStationDetail;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jusitn.Yeh
 */
@Repository
public class CellPassStationDetailDAO extends AbstractDao<Integer, CellPassStationDetail> implements BasicDAO_1<CellPassStationDetail> {

    @Override
    public List<CellPassStationDetail> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public CellPassStationDetail findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<CellPassStationDetail> findByDate(DateTime sD, DateTime eD) {
        return super.createEntityCriteria()
                .add(Restrictions.between("createDate", sD.toDate(), eD.toDate()))
                .list();
    }

    @Override
    public int insert(CellPassStationDetail pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<CellPassStationDetail> l) {
        Session session = super.getSession();
        int row = 0;
        for (CellPassStationDetail pojo : l) {
            session.save(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

    @Override
    public int update(CellPassStationDetail pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(CellPassStationDetail pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

    public int delete(List<CellPassStationDetail> l) {
        Session session = super.getSession();
        int row = 0;
        for (CellPassStationDetail pojo : l) {
            session.delete(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

}
