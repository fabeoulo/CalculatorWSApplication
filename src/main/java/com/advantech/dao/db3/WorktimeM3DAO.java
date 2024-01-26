/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.dao.db3;

import com.advantech.model.db3.WorktimeM3;
import com.advantech.model.view.db3.WorktimeCobots;
import java.util.List;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Justin.Yeh
 */
@Repository
public class WorktimeM3DAO extends AbstractDao<Integer, WorktimeM3DAO> {

    public List<WorktimeM3> findByModel(List<String> modelNames) {
        String sql = "select id,model_name \"modelName\", clean_panel \"cleanPanel\", total_module \"totalModule\", keypart_a \"keypartA\" from Worktime where model_name in (?)";
        Query query = super.queryIn(sql, modelNames);

        return query.setResultTransformer(Transformers.aliasToBean(WorktimeM3.class))
                .list();
    }

    public int update(WorktimeM3 pojo) {
        super.getSession().update(pojo);
        return 1;
    }
}
