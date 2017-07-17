/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.PassStation;
import com.advantech.model.PassStationDAO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Wei.Cheng
 */
public class PassStationService {

    private final PassStationDAO passStationDAO;

    protected PassStationService() {
        passStationDAO = new PassStationDAO();
    }

    public List<PassStation> getPassStation() {
        return passStationDAO.getPassStation();
    }

    public List<PassStation> getPassStation(String PO, String type) {
        return passStationDAO.getPassStation(PO, type);
    }

    public boolean insertPassStation(List<PassStation> l) {
        return passStationDAO.insertPassStation(l);
    }

    public List<Map> getCellLastGroupStatusView() {
        return passStationDAO.getCellLastGroupStatusView();
    }

    public List<Map> getAllCellPerPcsHistory(String PO, String type, Integer lineName, Integer minPcs, Integer maxPcs, String startDate, String endDate) {
        if (minPcs != null && maxPcs != null && minPcs > maxPcs) {
            maxPcs = maxPcs + minPcs;
            minPcs = maxPcs - minPcs;
            maxPcs = maxPcs - minPcs;
            //http://javarevisited.blogspot.com/2013/02/swap-two-numbers-without-third-temp-variable-java-program-example-tutorial.html#ixzz4SDLwO600
        }
        return passStationDAO.getAllCellPerPcsHistory(PO, type, lineName, minPcs, maxPcs, startDate, endDate);
    }
}
