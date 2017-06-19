/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.ModelResponsor;
import com.advantech.model.ModelResponsorDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class ModelResponsorService {

    private final ModelResponsorDAO modelResponsorDAO;

    protected ModelResponsorService() {
        modelResponsorDAO = new ModelResponsorDAO();
    }

    public List<ModelResponsor> getModelResponsor() {
        return modelResponsorDAO.getModelResponsor();
    }

    public List<Map> getModelResponsor1() {
        return modelResponsorDAO.getModelResponsor1();
    }

    public List<ModelResponsor> getModelResponsor(String modelName) {
        return modelResponsorDAO.getModelResponsor(modelName);
    }

    public String getModelResponsor(String departmentCode, String modelName) {
        return modelResponsorDAO.getModelResponsor(departmentCode, modelName);
    }
}