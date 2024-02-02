/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.AlarmDODAO;
import com.advantech.model.db1.AlarmDO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Justin.Yeh
 */
@Service
@Transactional
public class AlarmDOService {

    @Autowired
    private AlarmDODAO dao;

    public List<AlarmDO> findAll() {
        return dao.findAll();
    }

    public List<String> findAllDistinctCorrespondDO() {
        return dao.findAllDistinctColumn("correspondDO");
    }

    public List<AlarmDO> findAllByTablesAndDOs(List<String> tableIds, List<String> DOs) {
        List<AlarmDO> list = new ArrayList<>();
        if (!tableIds.isEmpty() && !DOs.isEmpty()) {
            try {
                list = dao.findAllByTablesAndDOs(tableIds, DOs);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    public AlarmDO findByPrimaryKey(Object obj_id) {
        return dao.findByPrimaryKey(obj_id);
    }

    public int insert(AlarmDO pojo) {
        return dao.insert(pojo);
    }

    public int update(AlarmDO pojo) {
        return dao.update(pojo);
    }

    public int delete(AlarmDO pojo) {
        return dao.delete(pojo);
    }

}
