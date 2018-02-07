/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.model.view.BabAvg;
import com.advantech.model.view.BabLastGroupStatus;
import com.advantech.model.view.SensorCurrentGroupStatus;
import com.advantech.model.view.UserInfoRemote;
import com.advantech.model.view.Worktime;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public class SqlViewDAO extends AbstractDao<Integer, Object> {

    public List<BabAvg> findBabAvg(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabAvg(:bab_id)")
                .addScalar("bab_id", StandardBasicTypes.INTEGER)
                .addScalar("station", StandardBasicTypes.INTEGER)
                .addScalar("average", StandardBasicTypes.DOUBLE)
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabAvg.class))
                .list();
    }

    public List<BabAvg> findBabAvgInHistory(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabAvg_history(:bab_id)")
                .addScalar("bab_id", StandardBasicTypes.INTEGER)
                .addScalar("station", StandardBasicTypes.INTEGER)
                .addScalar("average", StandardBasicTypes.DOUBLE)
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabAvg.class))
                .list();
    }

    public List<Worktime> findWorktime() {
        return super.getSession()
                .createCriteria(Worktime.class)
                .list();
    }

    public Worktime findWorktime(String modelName) {
        return (Worktime) super.getSession()
                .createCriteria(Worktime.class)
                .add(Restrictions.eq("modelName", modelName))
                .uniqueResult();
    }

    public UserInfoRemote findUserInfoRemote(String jobnumber) {
        return (UserInfoRemote) super.getSession()
                .createCriteria(UserInfoRemote.class)
                .add(Restrictions.eq("jobnumber", jobnumber))
                .uniqueResult();
    }

    public List<BabLastGroupStatus> findBabLastGroupStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLastGroupStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(BabLastGroupStatus.class))
                .list();
    }

    public List<SensorCurrentGroupStatus> findSensorCurrentGroupStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetSensorCurrentGroupStatus(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.aliasToBean(SensorCurrentGroupStatus.class))
                .list();
    }

    public List<Map> findSensorStatus(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_GetSensorStatus(:bab_id)")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findBalanceDetail(int bab_id) {
        return super.getSession()
                .createSQLQuery("select * from tbfn_BabBalanceDetail(:bab_id)")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Join detail with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findBabDetail(String lineTypeName, String sitefloorName, DateTime sD, DateTime eD, boolean isAboveStandard) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetBabDetail(:lineTypeName, :sitefloorName, :sD, :eD, :minPcs)}")
                .setParameter("lineTypeName", lineTypeName)
                .setParameter("sitefloorName", sitefloorName)
                .setParameter("sD", sD.withHourOfDay(0).toDate())
                .setParameter("eD", eD.withHourOfDay(23).toDate())
                .setParameter("minPcs", isAboveStandard ? 10 : "-1")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Get bananceCompare with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findLineBalanceCompareByBab(int bab_id) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLineBalanceCompareByBab(:bab_id)}")
                .setParameter("bab_id", bab_id)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    //Get bananceCompare with alarmPercent in /pages/admin/BabTotal page
    public List<Map> findLineBalanceCompare(String modelName, String lineTypeName) {
        return super.getSession()
                .createSQLQuery("{CALL usp_GetLineBalanceCompare(:modelName, :lineTypeName)}")
                .setParameter("modelName", modelName)
                .setParameter("lineTypeName", lineTypeName)
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

    public List<Map> findSensorStatusPerStationToday() {
        return super.getSession()
                .createSQLQuery("select * from vw_SensorStatusPerStationToday")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }
}