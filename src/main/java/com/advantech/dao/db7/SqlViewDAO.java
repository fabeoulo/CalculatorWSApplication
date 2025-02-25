/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao.db7;

import com.advantech.model.db1.Worktime;
import com.advantech.model.db1.WorktimeM6;
import java.util.List;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository(value = "sqlViewDAO7")
public class SqlViewDAO extends AbstractDao<Integer, Object> {

    private final String qrySheetMain = "select modelName, floorName, speOwnerName, eeOwnerName, qcOwnerName, "
                        + "assy assy, t1 t1, t2 t2, t3 t3, 0.0 t4, "
                        + "packing packing, pi preAssy, 1 assyPeople, 1 packingPeople, 0.0 packingLeadTime, "
                        + "clean_panel cleanPanel "
                        + "from Sheet_Main_view";
            
    public List<Worktime> findWorktime() {
        return super.getSession()
                .createSQLQuery(qrySheetMain)
                .setResultTransformer(Transformers.aliasToBean(Worktime.class))
                .list();
    }

    public List<WorktimeM6> findWorktimeM6() {
        return super.getSession()
                .createSQLQuery(qrySheetMain)
                .setResultTransformer(Transformers.aliasToBean(WorktimeM6.class))
                .list();
    }
}
