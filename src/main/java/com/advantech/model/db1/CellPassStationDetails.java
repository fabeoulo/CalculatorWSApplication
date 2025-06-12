/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model.db1;

import com.advantech.webservice.mes.RvQueryResult;
import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "NewDataSet")
public class CellPassStationDetails implements Serializable, RvQueryResult<CellPassStationDetail>{

    @XmlElement(name = "Table1", type = CellPassStationDetail.class)
    private List<CellPassStationDetail> QryData;


    @Override
    public List<CellPassStationDetail> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<CellPassStationDetail> QryData) {
        this.QryData = QryData;
    }

}
