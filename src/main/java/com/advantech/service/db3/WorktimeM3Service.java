/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.service.db3;

import com.advantech.dao.db3.WorktimeM3DAO;
import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.model.db3.WorktimeM3;
import static com.google.common.base.Preconditions.checkArgument;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Justin.Yeh
 */
@Service
@Transactional("transactionManager3")
public class WorktimeM3Service {

    @Autowired
    private WorktimeM3DAO worktimeM3DAO;

    public List<WorktimeM3> findByModel(List<String> modelNames) {
        return worktimeM3DAO.findByModel(modelNames);
    }

    public int update(WorktimeM3 pojo) {
        return worktimeM3DAO.update(pojo);
    }

    public void checkIsModelNameInWorktime(WorktimeM3 pojo) {
        List<WorktimeM3> l = worktimeM3DAO.findByModel(Arrays.asList(pojo.getModelName()));
        if (!l.isEmpty()) {
            WorktimeM3 existRecord = l.get(0);
            checkArgument(pojo.getId() == existRecord.getId(),
                    "modelName is not in WorktimeM3Service" );
        }
    }
}
