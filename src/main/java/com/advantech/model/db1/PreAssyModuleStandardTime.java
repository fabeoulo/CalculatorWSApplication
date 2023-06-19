/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "PreAssyModuleStandardTime")
public class PreAssyModuleStandardTime implements Serializable, Cloneable {

    private int id;
    private String modelName;
    private PreAssyModuleType preAssyModuleType;
    private BigDecimal standardTime;
    private String sopName;
    private String sopPage;
    private Date standardTimeModifyDate;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "modelName", nullable = false, length = 50, unique = true)
    public String getModelName() {
        return modelName == null ? null : modelName.trim();
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preAssyModuleType_id", nullable = false)
    public PreAssyModuleType getPreAssyModuleType() {
        return preAssyModuleType;
    }

    public void setPreAssyModuleType(PreAssyModuleType preAssyModuleType) {
        this.preAssyModuleType = preAssyModuleType;
    }

    @Column(name = "standardTime", nullable = false, precision = 10, scale = 1)
    public BigDecimal getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(BigDecimal standardTime) {
        this.standardTime = standardTime;
    }

    @Column(name = "sop_name", length = 50)
    public String getSopName() {
        return sopName;
    }

    public void setSopName(String sopName) {
        this.sopName = sopName;
    }

    @Column(name = "sop_page", length = 50)
    public String getSopPage() {
        return sopPage;
    }

    public void setSopPage(String sopPage) {
        this.sopPage = sopPage;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifyDate", length = 23, nullable = true)    
    public Date getStandardTimeModifyDate() {
        return standardTimeModifyDate;
    }

    public void setStandardTimeModifyDate(Date standardTimeModifyDate) {
        this.standardTimeModifyDate = standardTimeModifyDate;
    }

    @Override
    public PreAssyModuleStandardTime clone() throws CloneNotSupportedException {
        return (PreAssyModuleStandardTime) super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
