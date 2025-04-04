/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "LineType")
public class LineType implements Serializable {

    private int id;

    private String name;

    @JsonIgnore
    private Set<Line> lines = new HashSet<>(0);

    @JsonIgnore
    private Set<TestTable> testTables = new HashSet<>(0);
    
    @JsonIgnore
    private Set<LineTypeConfig> lineTypeConfigs = new HashSet<>(0);

    @JsonIgnore
    private Set<PreAssyModuleType> preAssyModuleTypes = new HashSet<>(0);

    @JsonIgnore
    private Set<PrepareScheduleDailyRemark> prepareScheduleDailyRemarks = new HashSet<>(0);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "[name]", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineType")
    public Set<Line> getLines() {
        return lines;
    }

    public void setLines(Set<Line> lines) {
        this.lines = lines;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineType")
    public Set<TestTable> getTestTables() {
        return testTables;
    }

    public void setTestTables(Set<TestTable> testTables) {
        this.testTables = testTables;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineType")
    public Set<LineTypeConfig> getLineTypeConfigs() {
        return lineTypeConfigs;
    }

    public void setLineTypeConfigs(Set<LineTypeConfig> lineTypeConfigs) {
        this.lineTypeConfigs = lineTypeConfigs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineType")
    public Set<PreAssyModuleType> getPreAssyModuleTypes() {
        return preAssyModuleTypes;
    }

    public void setPreAssyModuleTypes(Set<PreAssyModuleType> preAssyModuleTypes) {
        this.preAssyModuleTypes = preAssyModuleTypes;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineType")
    public Set<PrepareScheduleDailyRemark> getPrepareScheduleDailyRemarks() {
        return prepareScheduleDailyRemarks;
    }

    public void setPrepareScheduleDailyRemarks(Set<PrepareScheduleDailyRemark> prepareScheduleDailyRemarks) {
        this.prepareScheduleDailyRemarks = prepareScheduleDailyRemarks;
    }

}
