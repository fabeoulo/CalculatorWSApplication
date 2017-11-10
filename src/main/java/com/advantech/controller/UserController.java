/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 查看所屬人員是否存在
 */
package com.advantech.controller;

import com.advantech.model.Identit;
import com.advantech.service.IdentitService;
import java.io.PrintWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class UserController {

    @Autowired
    private IdentitService identitService;

    @RequestMapping(value = "/CheckUser", method = {RequestMethod.POST})
    @ResponseBody
    public boolean checkUser(@RequestParam String jobnumber) {
        return isUserExist(jobnumber);
    }

    private boolean isUserExist(String jobnumber) {
        //change the sql query(password not check)
        Identit i = identitService.getIdentit(jobnumber);
        return !(i == null);
    }

}