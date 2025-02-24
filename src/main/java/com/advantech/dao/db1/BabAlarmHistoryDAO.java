/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.BabAlarmHistory;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabAlarmHistoryDAO extends AbstractDao<Integer, BabAlarmHistory> implements BasicDAO_1<BabAlarmHistory> {

    @Override
    public List<BabAlarmHistory> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public BabAlarmHistory findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }
    
    public BabAlarmHistory findByBab(int bab_id){
        return (BabAlarmHistory) super.createEntityCriteria()
                .add(Restrictions.eq("bab.id", bab_id))
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public int insert(BabAlarmHistory pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int update(BabAlarmHistory pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int delete(BabAlarmHistory pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
