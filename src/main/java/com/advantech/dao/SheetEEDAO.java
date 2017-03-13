/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.dao;

import com.advantech.entity.SheetEe;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class SheetEEDAO extends BasicDAOImpl implements BasicDAO {

    private static final Logger log = LoggerFactory.getLogger(SheetViewDAO.class);

    public SheetEEDAO() {

    }

    @Override
    public Collection findAll() {
        return super.findAll("from SheetEe");
    }

    @Override
    public Object findByPrimaryKey(Object obj_id) {
        return super.findByPrimaryKey(SheetEe.class, integerToLong((Integer) obj_id));
    }

}