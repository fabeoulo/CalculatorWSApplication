/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db1;

import com.advantech.dao.HibernateQueryMainActions;
import static com.advantech.helper.HibernateBatchUtils.flushIfReachFetchSize;
import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author Wei.Cheng
 * @param <PK>
 * @param <T>
 */
public abstract class AbstractDao<PK extends Serializable, T> extends HibernateQueryMainActions<PK, T> {

    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void setSessionFactory() {
        super.setSessionFactory(sessionFactory);
    }

    protected String getSessionSchema() {
        Object schema = null;
        String defaultSchema = "dbo";
        schema = sessionFactory.getProperties().get("hibernate.default_schema");

        return schema == null ? defaultSchema : schema.toString();
    }

    public int update(List<T> l) {
        Session session = super.getSession();
        int currentRow = 1;
        for (T a : l) {
            session.update(a);
            flushIfReachFetchSize(session, currentRow++);
        }
        return 1;
    }
}
