/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
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
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "BabPcsDetailHistory")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BabPcsDetailHistory implements Serializable {

    private int id;
    private Bab bab;
    private SensorTransform tagName;
    private int station;
    private int groupid;
    private int diff;
    private Date lastUpdateTime;
    private BigDecimal avgDiff;
    private Integer isAdjust;
    private Integer diffAdj;

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
    @JoinColumn(name = "bab_id", nullable = false)
    public Bab getBab() {
        return bab;
    }

    public void setBab(Bab bab) {
        this.bab = bab;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagName", nullable = false)
    public SensorTransform getTagName() {
        return tagName;
    }

    public void setTagName(SensorTransform tagName) {
        this.tagName = tagName;
    }

    @Column(name = "station", nullable = false)
    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }

    @Column(name = "groupid", nullable = false)
    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    @Column(name = "diff", nullable = false)
    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }

    @CreationTimestamp
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastUpdateTime", length = 23, updatable = false)
    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Column(name = "avg", precision = 10, scale = 2, nullable = true)
    public BigDecimal getAvgDiff() {
        return avgDiff;
    }

    public void setAvgDiff(BigDecimal avgDiff) {
        this.avgDiff = avgDiff;
    }

    @Column(name = "isAdjust", nullable = true)
    public Integer getIsAdjust() { 
        return isAdjust;
    }

    public void setIsAdjust(Integer isAdjust) {
        this.isAdjust = isAdjust;
    }

    @Column(name = "diffAdj", nullable = true)
    public Integer getDiffAdj() {
        return diffAdj;
    }

    public void setDiffAdj(Integer diffAdj) {
        this.diffAdj = diffAdj;
    }

}
