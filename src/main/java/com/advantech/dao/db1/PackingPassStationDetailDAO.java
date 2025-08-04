/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.PackingPassStationDetail;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class PackingPassStationDetailDAO extends AbstractDao<Integer, PackingPassStationDetail> implements BasicDAO_1<PackingPassStationDetail> {

    @Override
    public List<PackingPassStationDetail> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public PackingPassStationDetail findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<PackingPassStationDetail> findByDate(DateTime sD, DateTime eD) {
        return super.createEntityCriteria()
                .add(Restrictions.between("createDate", sD.toDate(), eD.toDate()))
                .list();
    }

    @Override
    public int insert(PackingPassStationDetail pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<PackingPassStationDetail> l) {
        Session session = super.getSession();
        int row = 0;
        for (PackingPassStationDetail pojo : l) {
            session.save(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

    @Override
    public int update(PackingPassStationDetail pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(PackingPassStationDetail pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

    public int delete(List<PackingPassStationDetail> l) {
        Session session = super.getSession();
        int row = 0;
        for (PackingPassStationDetail pojo : l) {
            session.delete(pojo);
            flushIfReachFetchSize(session, row++);
        }
        return 1;
    }

}
