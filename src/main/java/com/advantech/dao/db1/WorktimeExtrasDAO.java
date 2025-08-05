/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.WorktimeExtras;
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
public class WorktimeExtrasDAO extends AbstractDao<String, WorktimeExtras> implements BasicDAO_1<WorktimeExtras> {

    @Override
    public List<WorktimeExtras> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public WorktimeExtras findByPrimaryKey(Object obj_id) {
        return super.getByKey((String) obj_id);
    }

    public WorktimeExtras findByModelName(String modelName) {
        return (WorktimeExtras) super.createEntityCriteria()
                .add(Restrictions.eq("modelName", modelName))
                .uniqueResult();
    }

    public List<WorktimeExtras> findNotZeroPackingLeadTime() {
        return super.createEntityCriteria()
                .add(Restrictions.ne("packingLeadTime", BigDecimal.ZERO))
                .list();
    }

    @Override
    public int insert(WorktimeExtras pojo) {
        this.getSession().save(pojo);
        return 1;
    }

    public int insert(List<WorktimeExtras> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (WorktimeExtras a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int update(WorktimeExtras pojo) {
        this.getSession().update(pojo);
        return 1;
    }

    public int update(List<WorktimeExtras> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (WorktimeExtras a : l) {
            session.update(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int delete(WorktimeExtras pojo) {
        this.getSession().delete(pojo);
        return 1;
    }

    public int deleteAll() {
        Session session = super.getSession();
        session.createQuery("delete from WorktimeExtras").executeUpdate();
        return 1;
    }

}
