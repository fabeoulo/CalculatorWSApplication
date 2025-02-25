/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.WorktimeM6;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class WorktimeM6DAO extends AbstractDao<String, WorktimeM6> implements BasicDAO_1<WorktimeM6> {

    @Override
    public List<WorktimeM6> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public WorktimeM6 findByPrimaryKey(Object obj_id) {
        return super.getByKey((String) obj_id);
    }

    public WorktimeM6 findByModelName(String modelName) {
        return (WorktimeM6) super.createEntityCriteria()
                .add(Restrictions.eq("modelName", modelName))
                .uniqueResult();
    }

    public List<WorktimeM6> findNotZeroPackingLeadTime() {
        return super.createEntityCriteria()
                .add(Restrictions.ne("packingLeadTime", BigDecimal.ZERO))
                .list();
    }

    @Override
    public int insert(WorktimeM6 pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    public int insert(List<WorktimeM6> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (WorktimeM6 a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int update(WorktimeM6 pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(WorktimeM6 pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

    public int deleteAll() {
        Session session = super.getSession();
        session.createQuery("delete from WorktimeM6").executeUpdate();
        return 1;
    }

}
