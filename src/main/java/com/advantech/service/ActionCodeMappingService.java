/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.ActionCodeMapping;
import com.advantech.model.ActionCodeMappingDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class ActionCodeMappingService {

    private final ActionCodeMappingDAO actionCodeMappingDAO;

    protected ActionCodeMappingService() {
        actionCodeMappingDAO = new ActionCodeMappingDAO();
    }

    public List<ActionCodeMapping> getActionCodeMapping() {
        return actionCodeMappingDAO.getActionCodeMapping();
    }

    public List<Map> getActionCodeMapping1() {
        return actionCodeMappingDAO.getActionCodeMapping1();
    }

    public List<ActionCodeMapping> getActionCodeMapping(int id) {
        return actionCodeMappingDAO.getActionCodeMapping(id);
    }

    public List<ActionCodeMapping> getActionCodeMappingByActionCode(int ac_id) {
        return actionCodeMappingDAO.getActionCodeMappingByActionCode(ac_id);
    }
}