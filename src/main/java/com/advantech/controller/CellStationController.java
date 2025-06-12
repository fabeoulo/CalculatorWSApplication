/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查詢測試桌狀態
 */
package com.advantech.controller;

import com.advantech.model.db1.CellStation;
import com.advantech.service.db1.CellStationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Jusitn.Yeh
 */
@Controller
@RequestMapping(value = "/CellStationController")
public class CellStationController {

    @Autowired
    private CellStationService cellStationService;
    
    @RequestMapping(value = "/findBySitefloor", method = {RequestMethod.GET})
    @ResponseBody
    protected List<CellStation> findBySitefloor(@RequestParam String sitefloor) {
        String floorName = sitefloor;
        return cellStationService.findBySitefloor(floorName);
    }

}
