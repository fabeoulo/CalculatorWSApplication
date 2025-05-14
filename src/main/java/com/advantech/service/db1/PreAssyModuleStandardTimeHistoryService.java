/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.PreAssyModuleStandardTimeHistoryDAO;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.PreAssyModuleStandardTimeHistory;
import static com.google.common.base.Preconditions.checkArgument;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class PreAssyModuleStandardTimeHistoryService {

    @Autowired
    private PreAssyModuleStandardTimeHistoryDAO dao;

    public List<PreAssyModuleStandardTimeHistory> findAll() {
        return dao.findAll();
    }

    public PreAssyModuleStandardTimeHistory findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(PreAssyModuleStandardTimeHistory pojo) {
        return dao.insert(pojo);
    }

    public int update(PreAssyModuleStandardTimeHistory pojo) {
        return dao.update(pojo);
    }

    public int update(List<PreAssyModuleStandardTimeHistory> l) {
        return dao.update(l);
    }

    public int delete(PreAssyModuleStandardTimeHistory pojo) {
        return dao.delete(pojo);
    }

}
