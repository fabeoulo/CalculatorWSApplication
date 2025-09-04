/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db3;

import com.advantech.model.db1.Worktime;
import com.advantech.model.view.db3.WorktimeCobots;
import com.advantech.model.db1.WorktimeExtras;
import java.util.List;
import java.util.Map;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository(value = "sqlViewDAO3")
public class SqlViewDAO extends AbstractDao<Integer, Object> {

    public List<Worktime> findWorktime() {
        return super.getSession()
                .createSQLQuery("SELECT modelName, floorName, speOwnerName, eeOwnerName, qcOwnerName, "
                        + "assy assy, t1 t1, t2 t2, t3 t3, t4 t4, "
                        + "packing packing, totalModule preAssy, assyStation assyPeople, packingStation packingPeople, packingLeadTime, "
                        + "cleanPanel cleanPanel, t0 t0 "
                        + "FROM Sheet_Main_view")
                .setResultTransformer(Transformers.aliasToBean(Worktime.class))
                .list();
    }

    public List<WorktimeCobots> findCobots(List<String> modelNames) {
        String sql = "SELECT modelName, cobots FROM vw_WorktimeCobots where modelName in (?)";
        Query query = this.queryIn(sql, modelNames);

        return query.setResultTransformer(Transformers.aliasToBean(WorktimeCobots.class))
                .list();
    }

    public List<WorktimeExtras> findExtras() {
        return super.getSession()
                .createSQLQuery("SELECT model_name modelName, floor_id floorId, work_center workCenter, unit_no unitNo, ct "
                        + "FROM vw_Atmc_M9ie_WorktimeExtra ")
                .setResultTransformer(Transformers.aliasToBean(WorktimeExtras.class))
                .list();
    }

    public List<Map> findPreAssyModule() {
        return super.getSession()
                .createSQLQuery("SELECT model_name modelName, module_no moduleNo, work_center workCenter, module_item_name moduleName, ct "
                        + "FROM vw_Atmc_M9ie_WorktimePreAssyModeule ")
                .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
                .list();
    }

}
