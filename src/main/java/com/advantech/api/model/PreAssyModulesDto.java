/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.api.model;

import java.io.Serializable;

/**
 *
 * @author Justin.Yeh
 */
public class PreAssyModulesDto implements Serializable {

    private String modelName;
    private String preAssyModule;

    public PreAssyModulesDto() {
    }

    public PreAssyModulesDto(String modelName, String preAssyModule) {
        this.modelName = modelName;
        this.preAssyModule = preAssyModule;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getPreAssyModule() {
        return preAssyModule;
    }

    public void setPreAssyModule(String preAssyModule) {
        this.preAssyModule = preAssyModule;
    }

}
