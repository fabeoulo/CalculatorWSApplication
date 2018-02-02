/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 測試頁面儲存用
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.TagNameComparison;
import com.advantech.quartzJob.CountermeasureAlarm;
import com.advantech.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping(value = "/TestController")
public class TestController {

    @Autowired
    private LineService lineService;

    @RequestMapping(value = "/testException", method = {RequestMethod.POST})
    @ResponseBody
    public String test1() {
        throw new RuntimeException("ex");
    }

    @RequestMapping(value = "/testException1", method = {RequestMethod.POST})
    @ResponseBody
    public String test2() throws Exception {
        throw new Exception("ex1");
    }

    @RequestMapping(value = "/testException3", method = {RequestMethod.GET})
    @ResponseBody
    public String test3() throws Exception {
        throw new Exception("ex3");
    }

    @RequestMapping(value = "/testDataTableResponse", method = {RequestMethod.GET})
    @ResponseBody
    public DataTableResponse testDataTableResponse() throws Exception {
        return new DataTableResponse(lineService.findAll());
    }

    @RequestMapping(value = "/testMailBody", method = {RequestMethod.GET})
    @ResponseBody
    public String testMailBody(@RequestParam int floor_id) throws Exception {
        CountermeasureAlarm c = new CountermeasureAlarm();
        return c.generateMailBody(floor_id);
    }

    @RequestMapping(value = "/testInputParam", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String testInputParam(
            @RequestParam(name = "sensor[id][lampSysTagName][name]", required = false) String p1,
            @RequestParam(name = "sensor.id.lampSysTagName.name", required = false) String p2,
            @ModelAttribute TagNameComparison p3
    ) throws Exception {
        HibernateObjectPrinter.print(p1, p2, p3);
        return "Test";
    }
}
