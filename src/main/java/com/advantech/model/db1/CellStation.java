/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Jusitn.Yeh
 */
@Entity
@Table(name = "CellStation")
public class CellStation implements Serializable {

    private int id;
    private String name;
    private Floor floor;
    private LineType lineType;
    private int mesStationId;

    @JsonIgnore
    private Set<CellLoginRecord> cellLoginRecords = new HashSet<CellLoginRecord>(0);

    @JsonIgnore
    private Set<CellStationRecord> cellStationRecords = new HashSet<CellStationRecord>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "[name]", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineType_id")
    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    @Column(name = "mesStationId", nullable = false)
    public int getMesStationId() {
        return mesStationId;
    }

    public void setMesStationId(int mesStationId) {
        this.mesStationId = mesStationId;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cellStation")
    public Set<CellLoginRecord> getCellLoginRecords() {
        return cellLoginRecords;
    }

    public void setCellLoginRecords(Set<CellLoginRecord> cellLoginRecords) {
        this.cellLoginRecords = cellLoginRecords;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cellStation")
    public Set<CellStationRecord> getCellStationRecords() {
        return cellStationRecords;
    }

    public void setCellStationRecords(Set<CellStationRecord> cellStationRecords) {
        this.cellStationRecords = cellStationRecords;
    }
}
