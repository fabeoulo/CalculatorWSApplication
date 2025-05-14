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
@Table(name = "PreAssyModuleStandardTimeHistory")
public class PreAssyModuleStandardTimeHistory implements Serializable, Cloneable {

    private int id;
    private PreAssyModuleStandardTime preAssyModuleStandardTime;
    private BigDecimal standardTime;
    private Integer totalPcs;
    private Integer totalOpTime;
    private Date updateTime;

    public PreAssyModuleStandardTimeHistory() {
    }

    public PreAssyModuleStandardTimeHistory(PreAssyModuleStandardTime preAssyModuleStandardTime, BigDecimal standardTime, Integer totalPcs, Integer totalOpTime) {
        this.preAssyModuleStandardTime = preAssyModuleStandardTime;
        this.standardTime = standardTime;
        this.totalPcs = totalPcs;
        this.totalOpTime = totalOpTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preAssyModuleStandardTime_id", nullable = false)
    public PreAssyModuleStandardTime getPreAssyModuleStandardTime() {
        return preAssyModuleStandardTime;
    }

    public void setPreAssyModuleStandardTime(PreAssyModuleStandardTime preAssyModuleStandardTime) {
        this.preAssyModuleStandardTime = preAssyModuleStandardTime;
    }

    @Column(name = "standardTime", nullable = false, precision = 10, scale = 1)
    public BigDecimal getStandardTime() {
        return standardTime;
    }

    public void setStandardTime(BigDecimal standardTime) {
        this.standardTime = standardTime;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updateTime", length = 23, nullable = true, insertable = false, updatable = false)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "total_opTime", nullable = false)
    public Integer getTotalOpTime() {
        return totalOpTime;
    }

    public void setTotalOpTime(Integer totalOpTime) {
        this.totalOpTime = totalOpTime;
    }

    @Column(name = "total_pcs", nullable = false)
    public Integer getTotalPcs() {
        return totalPcs;
    }

    public void setTotalPcs(Integer totalPcs) {
        this.totalPcs = totalPcs;
    }

}
