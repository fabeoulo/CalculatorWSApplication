/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.jqgrid.PageInfo;
import com.advantech.model.SheetView;
import com.advantech.model.Worktime;
import com.advantech.service.SheetViewService;
import com.advantech.service.WorktimeService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.jxls.util.TransformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Wei.Cheng
 */
@Controller
public class FileDownloadController {

    @Autowired
    private SheetViewService sheetViewService;
    
    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private ResourceLoader resourceLoader;

    @ResponseBody
    @RequestMapping(value = "/Worktime/excel", method = {RequestMethod.GET})
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ModelAndView generateExcel(PageInfo info) {
        // create some sample data
        info.setRows(Integer.MAX_VALUE);
        info.setSidx("id");
        info.setSord("asc");
        List<SheetView> l = sheetViewService.findAll(info);
        ModelAndView mav = new ModelAndView("ExcelRevenueSummary");
        mav.addObject("revenueData", l);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/Worktime/excelForSpe", method = {RequestMethod.GET})
//    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public void generateExcelForUpload(HttpServletResponse response, PageInfo info) throws IOException {

        Resource r = resourceLoader.getResource("classpath:excel-template\\Plant-sp matl status(M3).xls");

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Set-Cookie", "fileDownload=true; path=/");
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + r.getFilename() + "\""));

        info.setRows(Integer.MAX_VALUE);
        info.setSidx("id");
        info.setSord("asc");

        try (InputStream is = r.getInputStream()) {
            List<Worktime> l = worktimeService.findWithFullRelation(info);
            try (OutputStream os = response.getOutputStream()) {
                this.outputFile(l, is, os);
            }
        }
    }

    private void outputFile(List data, InputStream is, OutputStream os) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Context context = new Context();
        context.putVar("sheetViews", data);
        context.putVar("dateFormat", dateFormat);

        Transformer transformer = TransformerFactory.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();

        //避免Jexl2在javabean值為null時會log
        evaluator.getJexlEngine().setSilent(true);

        JxlsHelper helper = JxlsHelper.getInstance();
        helper.processTemplate(context, transformer);
    }
}
