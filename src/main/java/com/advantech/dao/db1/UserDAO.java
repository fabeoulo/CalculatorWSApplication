/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.model.db1.Floor;
import com.advantech.model.db1.User;
import com.advantech.security.State;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class UserDAO extends AbstractDao<Integer, User> implements BasicDAO_1<User> {

    @Override
    public List<User> findAll() {
        return super.createEntityCriteria().list();
    }

    @Override
    public User findByPrimaryKey(Object obj_id) {
        return super.getByKey((int) obj_id);
    }

    public User findByJobnumber(String jobnumber) {
        Criteria c = super.createEntityCriteria();
        c.add(Restrictions.eq("jobnumber", jobnumber));
        c.addOrder(Order.desc("id"));
        return (User) c.setMaxResults(1).uniqueResult();
    }

    public List<User> findLineOwner(int line_id) {
        return getSession()
                .createQuery("select u from User u left join u.lines line where line.id = :line_id")
                .setParameter("line_id", line_id)
                .list();
    }

    public List<User> findLineOwnerBySitefloor(int floor_id) {
        return getSession()
                .createQuery("select distinct u from User u left join u.lines line where line.floor.id = :floor_id")
                .setParameter("floor_id", floor_id)
                .list();
    }

    public List<User> findByUserNotification(String notification_name) {
        return getSession()
                .createQuery("select u from User u join u.userNotifications noti where noti.name = :notification_name")
                .setParameter("notification_name", notification_name)
                .list();
    }

    public List<User> findByUserNotificationAndNotLineOwner(String notification_name) {
        return getSession()
                .createQuery("select u from User u join u.userNotifications noti "
                        + "where noti.name = :notification_name "
                        + "and u.lines IS EMPTY")
                .setParameter("notification_name", notification_name)
                .list();
    }

    public List<User> findByUserNotificationAndNotLineOwner(int floor_id, String notification_name) {
        return getSession()
                .createQuery("select u from User u join u.userNotifications noti "
                        + "where noti.name = :notification_name "
                        + "and floor.id = floor_id "
                        + "and u.lines IS EMPTY")
                .setParameter("notification_id", notification_name)
                .list();
    }

    public List<User> findByFloor(Floor f) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("floor", f))
                .list();
    }

    public List<User> findByRole(String... roleName) {
        return super.createEntityCriteria()
                .createAlias("userProfiles", "up")
                .add(Restrictions.in("up.name", roleName))
                .add(Restrictions.eq("state", State.ACTIVE))
                .list();
    }

    public List<User> findByFloorAndRole(Floor f, String... roleName) {
        return super.createEntityCriteria()
                .createAlias("userProfiles", "up")
                .add(Restrictions.eq("floor", f))
                .add(Restrictions.in("up.name", roleName))
                .add(Restrictions.eq("state", State.ACTIVE))
                .addOrder(Order.asc("usernameCh"))
                .list();
    }

    public List<User> findByState(State state) {
        return super.createEntityCriteria()
                .add(Restrictions.eq("state", state))
                .addOrder(Order.asc("usernameCh"))
                .list();
    }

    @Override
    public int insert(User pojo) {
        super.getSession().save(pojo);
        return 1;
    }

    @Override
    public int update(User pojo) {
        super.getSession().update(pojo);
        return 1;
    }

    @Override
    public int delete(User pojo) {
        super.getSession().delete(pojo);
        return 1;
    }

}
