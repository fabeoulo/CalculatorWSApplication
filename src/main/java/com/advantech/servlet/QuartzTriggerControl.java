/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 首頁雙擊出現的選單可控制此專案的核心"Quartz的暫停與開始"(有風險，請將連結到此servlet的前端加強保護)
 */
package com.advantech.servlet;

import com.advantech.helper.CronTrigMod;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@WebServlet(name = "QuartzTriggerControl", urlPatterns = {"/QuartzTriggerControl"})
public class QuartzTriggerControl extends HttpServlet {
    
    private static final Logger log = LoggerFactory.getLogger(QuartzTriggerControl.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String order = req.getParameter("order");
        if (order != null && !"".equals(order)) {
            String remoteIp = req.getRemoteAddr();
            out.print(CronTrigMod.getInstance().triggerPauseOrResume(order) ? "success" : "fail");
            log.info("Someone change the flag to :" + order + " --- " + remoteIp);
        }
    }

}