/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import com.advantech.model.db1.ReplyStatus;
import com.advantech.model.db1.TestRecord;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class TestRecordDAO extends AbstractDao<Integer, TestRecord> implements BasicDAO_1<TestRecord> {

    @Override
    public List<TestRecord> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public TestRecord findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<TestRecord> findByDate(DateTime sD, DateTime eD, boolean unReplyOnly) {
        if (eD.getHourOfDay() == 0) {
            sD = sD.withHourOfDay(0);
            eD = eD.plusDays(1);
        }
        Criteria c = super.createEntityCriteria();
        c.setFetchMode("testTable", FetchMode.JOIN);
        c.add(Restrictions.between("lastUpdateTime", sD.toDate(), eD.toDate()));

        if (unReplyOnly) {
            c.add(Restrictions.eq("replyStatus", ReplyStatus.UNREPLIED));
        }

        return c.list();
    }

    @Override
    public int insert(TestRecord pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    public int insert(List<TestRecord> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (TestRecord a : l) {
            session.save(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }

    @Override
    public int update(TestRecord pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(TestRecord pojo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
