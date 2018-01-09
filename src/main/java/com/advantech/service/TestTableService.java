/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.TestTableDAO;
import com.advantech.model.TestTable;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class TestTableService {

    @Autowired
    private TestTableDAO testTableDAO;

    public List<TestTable> findAll() {
        return testTableDAO.findAll();
    }

    public TestTable findByPrimaryKey(Object obj_id) {
        return testTableDAO.findByPrimaryKey(obj_id);
    }

    public List<TestTable> findBySitefloor(int sitefloor) {
        return testTableDAO.findBySitefloor(sitefloor);
    }

    public TestTable findByJobnumber(String jobnumber) {
        return testTableDAO.findByJobnumber(jobnumber);
    }

    public int insert(TestTable pojo) {
        return testTableDAO.insert(pojo);
    }

    public int update(TestTable pojo) {
        return testTableDAO.update(pojo);
    }

    public int delete(TestTable pojo) {
        return testTableDAO.delete(pojo);
    }

}
