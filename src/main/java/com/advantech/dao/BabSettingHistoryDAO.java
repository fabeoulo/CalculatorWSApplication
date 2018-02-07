/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.Bab;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.SensorTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class BabSettingHistoryDAO extends AbstractDao<Integer, BabSettingHistory> implements BasicDAO_1<BabSettingHistory> {

    @Override
    public List<BabSettingHistory> findAll() {
        return super.createEntityCriteria().list();
    }

    public List<Map> findAll(String po, Integer line_id,
            boolean findWithBalance, boolean findWithMininumAlarmPercent, Integer minPcs) {
        String sql = "";

        if (findWithBalance) {
            sql = "select new Map(bsh as babAlarmHistory, bah.balance as balance, bah.totalPcs as totalPcs) from Bab b "
                    + " join b.babAlarmHistorys bah "
                    + " join b.babSettingHistorys bsh"
                    + " where b.po = :po"
                    + " and (:line_id is null or :line_id = 0 or b.line.id = :line_id)"
                    + " and bah.balance = ("
                    + " select max(bah2.balance) from BabAlarmHistory bah2 "
                    + " join bah2.bab b2 "
                    + " where b2.po = :po and "
                    + " (:line_id is null or :line_id = 0 or b2.line.id = :line_id)"
                    + " and bah2.totalPcs >= :minPcs)"
                    + " and bah.totalPcs >= :minPcs";
        } else if (findWithMininumAlarmPercent) {
            sql = "select new Map(bsh as babAlarmHistory, bah.failPcs * 1.0 / bah.totalPcs as alarmPercent, bah.totalPcs as totalPcs) from Bab b "
                    + " join b.babAlarmHistorys bah "
                    + " join b.babSettingHistorys bsh"
                    + " where b.po = :po and (:line_id is null or :line_id = 0 or b.line.id = :line_id)"
                    + " and (bah.failPcs * 1.0 / bah.totalPcs) = ("
                    + " select min(bah2.failPcs * 1.0 / bah2.totalPcs) from BabAlarmHistory bah2 "
                    + " join bah2.bab b2 where b2.po = :po"
                    + " and (:line_id is null or :line_id = 0 or b2.line.id = :line_id)"
                    + " and bah2.totalPcs >= :minPcs))"
                    + " and bah.totalPcs >= :minPcs";
        }

        if (!findWithBalance && !findWithMininumAlarmPercent) {
            return new ArrayList();
        }

        Query q = super.getSession().createQuery(sql);
        q.setParameter("po", po);
        q.setParameter("line_id", line_id);
        q.setParameter("minPcs", minPcs);
        return q.list();
    }

    @Override
    public BabSettingHistory findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public List<BabSettingHistory> findByBab(Bab b) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("bab.id", b.getId()))
                .list();
    }

    public BabSettingHistory findByBabAndStation(Bab b, int station) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("bab.id", b.getId()));
        c.add(Restrictions.eq("station", station));
        return (BabSettingHistory) c.uniqueResult();
    }

    public List<BabSettingHistory> findProcessing() {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.isNull("lastUpdateTime"));
        return c.list();
    }

    public BabSettingHistory findProcessingByTagName(SensorTransform tagName) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("tagName", tagName));
        c.add(Restrictions.isNull("lastUpdateTime"));
        c.add(Restrictions.gt("createTime", new DateTime().withHourOfDay(0).toDate()));
        c.setMaxResults(1);
        return (BabSettingHistory) c.uniqueResult();
    }

    //Find the mininum bab_id per tagName
    public List<BabSettingHistory> findProcessingByLine(String lineName) {
        return super.getSession().createQuery(
                "from BabSettingHistory bsh join bsh.bab b join b.line l "
                + "where bsh.id in( "
                + "select min(bsh1.id) from BabSettingHistory bsh1 "
                + "join bsh1.bab b2 join b2.line l2 "
                + "where b2.babStatus is null "
                + "and l2.name = :lineName "
                + "and bsh1.lastUpdateTime is null "
                + "group by bsh1.tagName) "
                + "order by bsh.tagName")
                .setParameter("lineName", lineName)
                .list();
    }

    public List<BabSettingHistory> findProcessingByLine(int line_id) {
        return super.createEntityCriteria()
                .createAlias("bab", "b")
                .createAlias("b.line", "l")
                .createAlias("l.lineType", "lt")
                .add(Restrictions.isNull("b.babStatus"))
                .add(Restrictions.gt("b.beginTime", new DateTime().withHourOfDay(0).toDate()))
                .add(Restrictions.eq("l.id", line_id))
                .addOrder(Order.asc("b.id"))
                .list();
    }

    @Override
    public int insert(BabSettingHistory pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(BabSettingHistory pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(BabSettingHistory pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}