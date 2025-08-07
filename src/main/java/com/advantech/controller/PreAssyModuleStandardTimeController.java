/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡組裝包裝個線別第一站保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.model.db1.User;
import com.advantech.quartzJob.SyncPreAssyModuleFromRemote;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng Line login and logout function are Deprecated
 */
@Controller
@RequestMapping(value = "/PreAssyModuleStandardTimeController")
public class PreAssyModuleStandardTimeController {

    @Autowired
    private PreAssyModuleStandardTimeService service;

    @Autowired
    private SyncPreAssyModuleFromRemote syncJob;

    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findAll(HttpServletRequest request) {
        List l = service.findAll();
        return new DataTableResponse(l);
    }

    @RequestMapping(value = "/saveOrUpdate", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveOrUpdate(@Valid @ModelAttribute PreAssyModuleStandardTime pojo) {
        service.checkIsPreAssyModuleTypeExists(pojo);
        if (pojo.getId() == 0) {
            service.insert(pojo);
        } else {
            service.update(pojo);
        }
        return "success";
    }

    @RequestMapping(value = "/saveBySeries", method = {RequestMethod.POST})
    @ResponseBody
    protected String saveBySeries(@RequestParam String baseModelName, @RequestParam String targetModelName) throws CloneNotSupportedException {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        service.insertBySeries(baseModelName, targetModelName, user.getFloor());
        return "success";
    }

    @RequestMapping(value = "/delete", method = {RequestMethod.POST})
    @ResponseBody
    protected String delete(@RequestParam int id) {
        PreAssyModuleStandardTime pojo = service.findByPrimaryKey(id);
        service.delete(pojo);
        return "success";
    }

    @RequestMapping(value = "/syncPreAssyModule", method = {RequestMethod.POST})
    @ResponseBody
    protected String syncPreAssyModule() {
        syncJob.execute();
        return "success";
    }

}
