/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng vw_worktime
 */
@Entity
@Table(name = "vTb_WorkTime_M6")
public class WorktimeM6 implements Serializable {

    private int id;
    private String modelName;
    private String floorName;
    private String speOwnerName;
    private String eeOwnerName;
    private String qcOwnerName;
    private BigDecimal preAssy;
    private BigDecimal cleanPanel;
    private BigDecimal assy;
    private BigDecimal t1;
    private BigDecimal t2;
    private BigDecimal t3;
    private BigDecimal t4;
    private BigDecimal packing;
    private int assyPeople;
    private int packingPeople;
    private BigDecimal packingLeadTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "modelName", unique = true)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name = "floorName")
    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    @Column(name = "speOwnerName")
    public String getSpeOwnerName() {
        return speOwnerName;
    }

    public void setSpeOwnerName(String speOwnerName) {
        this.speOwnerName = speOwnerName;
    }

    @Column(name = "eeOwnerName")
    public String getEeOwnerName() {
        return eeOwnerName;
    }

    public void setEeOwnerName(String eeOwnerName) {
        this.eeOwnerName = eeOwnerName;
    }

    @Column(name = "qcOwnerName")
    public String getQcOwnerName() {
        return qcOwnerName;
    }

    public void setQcOwnerName(String qcOwnerName) {
        this.qcOwnerName = qcOwnerName;
    }

    @Column(name = "preAssy")
    public BigDecimal getPreAssy() {
        return preAssy;
    }

    public void setPreAssy(BigDecimal preAssy) {
        this.preAssy = preAssy;
    }

    @Column(name = "cleanPanel")
    public BigDecimal getCleanPanel() {
        return cleanPanel;
    }

    public void setCleanPanel(BigDecimal cleanPanel) {
        this.cleanPanel = cleanPanel;
    }

    @Column(name = "assy")
    public BigDecimal getAssy() {
        return assy;
    }

    public void setAssy(BigDecimal assy) {
        this.assy = assy;
    }

    @Column(name = "t1")
    public BigDecimal getT1() {
        return t1;
    }

    public void setT1(BigDecimal t1) {
        this.t1 = t1;
    }

    @Column(name = "t2")
    public BigDecimal getT2() {
        return t2;
    }

    public void setT2(BigDecimal t2) {
        this.t2 = t2;
    }

    @Column(name = "t3")
    public BigDecimal getT3() {
        return t3;
    }

    public void setT3(BigDecimal t3) {
        this.t3 = t3;
    }

    @Column(name = "t4")
    public BigDecimal getT4() {
        return t4;
    }

    public void setT4(BigDecimal t4) {
        this.t4 = t4;
    }

    @Column(name = "packing")
    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    @Column(name = "assyPeople")
    public int getAssyPeople() {
        return assyPeople;
    }

    public void setAssyPeople(int assyPeople) {
        this.assyPeople = assyPeople;
    }

    @Column(name = "packingPeople")
    public int getPackingPeople() {
        return packingPeople;
    }

    public void setPackingPeople(int packingPeople) {
        this.packingPeople = packingPeople;
    }

    @Column(name = "packingLeadTime")
    public BigDecimal getPackingLeadTime() {
        return packingLeadTime;
    }

    public void setPackingLeadTime(BigDecimal packingLeadTime) {
        this.packingLeadTime = packingLeadTime;
    }

}
