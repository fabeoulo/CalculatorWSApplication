/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 儲存CELL登入紀錄
 */
package com.advantech.controller;

import com.advantech.service.db1.CellLoginRecordService;
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
@RequestMapping(value = "/CellLoginRecordController")
public class CellLoginRecordController {

    @Autowired
    private CellLoginRecordService cellLoginRecordService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ResponseBody
    public String login(
            @RequestParam String jobnumber,
            @RequestParam int tableNo
    ) {
        cellLoginRecordService.insert(tableNo, jobnumber);
        return "success";
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST})
    @ResponseBody
    public String logout(@RequestParam String jobnumber) {
        cellLoginRecordService.delete(jobnumber);
        return "success";
    }

    @RequestMapping(value = "/changeDesk", method = {RequestMethod.POST})
    @ResponseBody
    public String changeDesk(
            @RequestParam String jobnumber,
            @RequestParam int tableNo
    ) {
        cellLoginRecordService.changeDeck(jobnumber);
        return "success";
    }
}
