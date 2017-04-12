/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.dao.*;
import com.advantech.model.Type;
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
public class TypeService {

    @Autowired
    private TypeDAO typeDAO;

    public List<Type> findAll() {
        return (List<Type>) typeDAO.findAll();
    }

    public Type findByPrimaryKey(Object obj_id) {
        return (Type) typeDAO.findByPrimaryKey(obj_id);
    }

    public int insert(Type type) {
        return typeDAO.insert(type);
    }

    public int update(Type type) {
        return typeDAO.update(type);
    }

    public int delete(Type type) {
        return typeDAO.delete(type);
    }

}