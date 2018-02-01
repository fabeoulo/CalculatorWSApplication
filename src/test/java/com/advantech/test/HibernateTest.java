/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.test;

import com.advantech.dao.AlarmTestActionDAO;
import com.advantech.dao.BabDAO;
import com.advantech.dao.SqlViewDAO;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.AlarmBabAction;
import com.advantech.model.Bab;
import com.advantech.model.BabAlarmHistory;
import com.advantech.model.BabSensorLoginRecord;
import com.advantech.model.BabSettingHistory;
import com.advantech.model.BabStatus;
import com.advantech.model.CountermeasureEvent;
import com.advantech.model.ReplyStatus;
import com.advantech.model.SensorTransform;
import com.advantech.model.TagNameComparison;
import com.advantech.model.User;
import com.advantech.security.State;
import com.advantech.service.BabPcsDetailHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private AlarmTestActionDAO alarmTestActionDAO;

    @Autowired
    private BabDAO babDAO;

    @Autowired
    private com.advantech.dao.TestDAO testDAO;

    @Autowired
    private BabPcsDetailHistoryService pcsHistoryService;

//    @Test
    @Transactional
    @Rollback(true)
    public void test() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Query q = session.createQuery("select u from User u join u.userNotifications notifi where notifi.id = 1");
        List l = q.list();
        assertTrue(l.get(0) instanceof User);
        assertEquals(13, l.size());
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void test2() throws JsonProcessingException {
        Object o = alarmTestActionDAO.findByPrimaryKey("T1");
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void test3() throws JsonProcessingException {
        Object o = babDAO.findByPrimaryKey(11035);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlQuery() {
        Query q = sessionFactory.getCurrentSession().createSQLQuery("select t.* from testView t");
        q.setResultTransformer(Transformers.aliasToBean(Bab.class));
        List l = q.list();
        assertEquals(10, l.size());
        assertTrue(l.get(0) instanceof Bab);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testQuery() throws JsonProcessingException {
        testObject(
                //                Floor.class,
                User.class,
                //                UserProfile.class,
                //                UserNotification.class,
                //                Unit.class,
                //                TestTable.class,
                //                com.advantech.model.Test.class,
                //                TestRecord.class,
                //                AlarmTestAction.class,
                //                AlarmBabAction.class,
                //                Line.class,
                //                LineType.class,
                //                LineTypeConfig.class,
                //                Bab.class,
                //                Fbn.class,
                //                BabPcsDetailHistory.class,
                //                BabSettingHistory.class,
                //                BabBalanceHistory.class,
                //                BabAlarmHistory.class,
                //                Countermeasure.class,
                //                CountermeasureEvent.class,
                //                ActionCode.class,
                //                ErrorCode.class,
                CountermeasureEvent.class
        );

    }

    private void testObject(Class... cls) {
        Session session = sessionFactory.getCurrentSession();
        for (Class c : cls) {
            System.out.println(c);
            Object o = session.createCriteria(c).setMaxResults(1).uniqueResult();
            assertNotNull(o);
//            assertEquals(o.getClass(), c);
            System.out.println(c + " Test pass");
        }
    }

    @Autowired
    private SqlViewDAO sqlViewDAO;

//    @Test
    @Transactional
    @Rollback(true)
    public void testSqlFunction() throws JsonProcessingException {
        Object o = sqlViewDAO.findBabAvg(10870);
        assertNotNull(o);
        HibernateObjectPrinter.print(o);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testFbn() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(TagNameComparison.class);
        c.add(Restrictions.eq("id.lampSysTagName.name", "L4-S-1"));
        HibernateObjectPrinter.print(c.list());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testHql() throws JsonProcessingException {
        String hql = "select new Map(b.id as id, b.po as po, b.modelName as modelName, "
                + "l.name as lineName, f.name as floorName) "
                + "from Bab b left join b.line l left join l.floor f";
        Query q = sessionFactory.getCurrentSession().createQuery(hql);
        q.setMaxResults(10);
        List l = q.list();
        HibernateObjectPrinter.print(l);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void chartTest() throws JsonProcessingException {
        List l = pcsHistoryService.findByBabForMap(12595);
        HibernateObjectPrinter.print(this.toChartForm(l));
    }

    private Map toChartForm(List<Map> l) {
        List<Map<String, Object>> total = new ArrayList();
        int diffSum = 0;
        int maxGroup = 0;
        for (Map m : l) {
            String tagName = (String) m.get("tagName");
            Integer groupid = (Integer) m.get("groupid");
            Integer diff = (Integer) m.get("diff");
            Map filter = total.stream()
                    .filter(i -> i.containsKey("name") && i.get("name").equals(tagName))
                    .findFirst().orElse(null);
            if (filter == null) {
                Map tagInfo = new HashMap();
                tagInfo.put("name", tagName);
                List dataPoints = new ArrayList();
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", diff);
                tagInfo.put("dataPoints", dataPoints);
                total.add(tagInfo);
            } else {
                List dataPoints = (List) filter.get("dataPoints");
                Map dataPoint = new HashMap();
                dataPoint.put("x", groupid);
                dataPoint.put("y", m.get("diff"));
                dataPoints.add(dataPoint);
            }
            diffSum += diff;
            if (maxGroup < groupid) {
                maxGroup = groupid;
            }
        }

        int people = total.size();

        Map infoWithAvg = new HashMap();
        infoWithAvg.put("avg", (diffSum / people / maxGroup));
        infoWithAvg.put("data", total);
        return infoWithAvg;
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testConverter() {
        Session session = sessionFactory.getCurrentSession();
        Bab bab = (Bab) session.get(Bab.class, 12730);
        assertNotNull(bab);
        assertNotNull(bab.getBabStatus());
        assertEquals(bab.getBabStatus(), BabStatus.UNFINSHED);
        assertNotNull(bab.getReplyStatus());
        assertEquals(bab.getReplyStatus(), ReplyStatus.UNREPLIED);

        bab.setBabStatus(BabStatus.CLOSED);
        bab.setReplyStatus(ReplyStatus.NO_NEED_TO_REPLY);

        session.update(bab);

        HibernateObjectPrinter.print(bab);
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testTagNameCompar() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        SensorTransform sensor = session.get(SensorTransform.class, "Cell-15-S-1");
        assertNotNull(sensor);
        sensor.setName("Cell-15-S-1*");
        session.save(sensor);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabSettingHistory() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        SensorTransform sensor = session.get(SensorTransform.class, "L1-S-1");
        assertNotNull(sensor);
        BabSensorLoginRecord loginRec = new BabSensorLoginRecord(sensor, "A-7568");
        session.save(loginRec);
        assertNotEquals(0, loginRec.getId());
        assertNotNull(loginRec.getBeginTime());
        HibernateObjectPrinter.print(loginRec);

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testBabProcessing() throws JsonProcessingException {
        Session session = sessionFactory.getCurrentSession();
        AlarmBabAction a = session.get(AlarmBabAction.class, "LD-L-4");
        assertNotNull(a);
        a.setAlarm(1);
        session.save(a);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void userPswRefractor() {
        Session session = sessionFactory.getCurrentSession();
        List<User> l = session.createCriteria(User.class).list();
        assertTrue(!l.isEmpty());
        l.stream().map((u) -> {
            u.setState(State.ACTIVE);
            return u;
        }).forEachOrdered((u) -> {
            session.update(u);
        });
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testQueryEnumField() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(Bab.class);
        c.add(Restrictions.eq("replyStatus", ReplyStatus.UNREPLIED));
        List l = c.list();
        assertEquals(375, l.size());
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testCriteria() {
        Criteria c = sessionFactory.getCurrentSession().createCriteria(Bab.class);
        c.createAlias("line", "l");
        c.createAlias("l.lineType", "lt");
        c.createAlias("babAlarmHistorys", "h");
        c.createAlias("l.users", "u");
        c.add(Restrictions.eq("replyStatus", ReplyStatus.UNREPLIED));
        c.add(Restrictions.eq("l.floor.id", 1));
        List<Bab> l = c.list();
        Bab b = l.get(0);
        Set<User> s1 = b.getLine().getUsers();
        Set<BabAlarmHistory> s2 = b.getBabAlarmHistorys();
        assertTrue(!s1.isEmpty());
        assertTrue(!s2.isEmpty());
        HibernateObjectPrinter.print(l.get(0));
        HibernateObjectPrinter.print(s1);
        HibernateObjectPrinter.print(s2);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testQuery2() {
        Session session = sessionFactory.getCurrentSession();

        String po = "PAH2070ZA";

        Query q = session.createQuery(
                "select bah from BabAlarmHistory bah"
                + " join bah.bab b"
                + " where b.po = :po"
                + " order by balance desc, b.id desc");

        q.setParameter("po", po);
        q.setMaxResults(1);
        List l = q.list();
        assertTrue(!l.isEmpty());
        HibernateObjectPrinter.print(l);
    }
}
