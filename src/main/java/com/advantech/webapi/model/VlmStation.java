/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Justin.Yeh
 */
public class VlmStation {

    public String wo;
    @JsonProperty("OP_ID")
    public String jobNumber;
    public String station;
    public int people;
    @JsonProperty("pre")
    public boolean isPre;

    public VlmStation() {
    }

    public VlmStation(String wo, String jobNumber, String station, int people, boolean isPre) {
        this.wo = wo;
        this.jobNumber = jobNumber;
        this.station = station;
        this.people = people;
        this.isPre = isPre;
    }

    public String getWo() {
        return wo;
    }

    public void setWo(String wo) {
        this.wo = wo;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public boolean isIsPre() {
        return isPre;
    }

    public void setIsPre(boolean isPre) {
        this.isPre = isPre;
    }

}
