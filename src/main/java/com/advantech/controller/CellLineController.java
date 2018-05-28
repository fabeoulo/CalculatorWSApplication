/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 卡Cell各線別保持"唯一"用
 */
package com.advantech.controller;

import com.advantech.model.CellLine;
import com.advantech.service.CellLineService;
import com.google.gson.Gson;
import java.io.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class CellLineController {

    private static final Logger log = LoggerFactory.getLogger(CellLineController.class);

    @Autowired
    private CellLineService cellLineService;

    private final String LOGIN = "LOGIN";
    private final String LOGOUT = "LOGOUT";

    @RequestMapping(value = "/CellLineServlet/findAll", method = {RequestMethod.GET})
    @ResponseBody
    protected List<CellLine> findAll() {
        return cellLineService.findAll();
    }

    @RequestMapping(value = "/CellLineServlet/findBySitefloor", method = {RequestMethod.GET})
    @ResponseBody
    protected List<CellLine> findBySitefloor(@RequestParam(required = true) int sitefloor) {
        return cellLineService.findBySitefloor(sitefloor);
    }

    @RequestMapping(value = "/CellLineServlet", method = {RequestMethod.GET})
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        List<CellLine> l = cellLineService.findAll();
        JSONArray arr = new JSONArray();
        for (CellLine cellLine : l) {
            arr.put(new JSONObject().put(Integer.toString(cellLine.getId()), cellLine.getName()));
        }

        out.println(arr);

    }

    @RequestMapping(value = "/CellLineServlet", method = {RequestMethod.POST})
    protected void doPost(
            @RequestParam int lineId,
            @RequestParam String action,
            HttpServletResponse res
    ) throws ServletException, IOException {

        res.setContentType("text/plain");

        PrintWriter out = res.getWriter();

        CellLine cellLine = cellLineService.findOne(lineId);
        String msg;
        switch (action) {
            case LOGIN:
                if (cellLine.isOpened()) {
                    msg = "This line is already in used";
                } else if (cellLine.isLocked()) {
                    msg = "This line is locked right now";
                } else {
                    msg = cellLineService.login(lineId) ? "success" : "fail";
                }
                break;
            case LOGOUT:
                if (cellLine.isOpened()) {
                    msg = cellLineService.logout(lineId) ? "success" : "fail";
                } else {
                    msg = "This line is already closed";
                }
                break;
            case "select":
                msg = new Gson().toJson(cellLine);
                break;
            default:
                msg = "未知的動作。";
                break;
        }
        out.print(msg);

    }
}