/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Bab;
import com.advantech.model.BabPassStationRecord;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabPassStationRecordDAO extends AbstractDao<Integer, BabPassStationRecord> implements BasicDAO_1<BabPassStationRecord> {

    @Override
    public List<BabPassStationRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public BabPassStationRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public List<BabPassStationRecord> findByBabAndBarcode(Bab b, String barcode){
        return super.createEntityCriteria()
                .add(Restrictions.eq("bab.id", b.getId()))
                .add(Restrictions.eq("barcode", barcode))
                .list();
    }
    
    public List<BabPassStationRecord> findByBab(Bab b){
        return super.createEntityCriteria()
                .add(Restrictions.eq("bab.id", b.getId()))
                .list();
    }

    @Override
    public int insert(BabPassStationRecord pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(BabPassStationRecord pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BabPassStationRecord pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

}