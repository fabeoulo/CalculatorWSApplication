/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import java.io.Serializable;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 * Cell passstation record
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Table1")
public class PassStation implements Serializable {

    @XmlElement(name = "BARCODE_NO")
    private String barcode;

    @XmlElement(name = "WIP_NO")
    private String PO;

    @XmlElement(name = "LINE_DESC")
    private String lineName;

    @XmlElement(name = "LINE_ID")
    private Integer lineId;

    @XmlElement(name = "STATION_DESC")
    private String station;

    @XmlElement(name = "CREATE_DATE")
    private String createDate;

    @XmlElement(name = "USER_NO")
    private String userNo;

    @XmlElement(name = "USER_NAME_CH")
    private String userName;

    private String type;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.barcode);
        hash = 89 * hash + Objects.hashCode(this.lineName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PassStation other = (PassStation) obj;
        if (!Objects.equals(this.barcode, other.barcode)) {
            return false;
        }
        if (!Objects.equals(this.lineName, other.lineName)) {
            return false;
        }
        return true;
    }

}
