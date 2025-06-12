/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 取得Cell站別登入紀錄
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import static com.advantech.helper.SecurityPropertiesUtils.retrieveAndCheckUserInSession;
import com.advantech.model.db1.ReplyStatus;
import com.advantech.model.db1.CellStationRecord;
import com.advantech.model.db1.User;
import com.advantech.service.db1.CellStationRecordService;
import java.io.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping(value = "/CellStationRecordController")
public class CellStationRecordController {

    @Autowired
    private CellStationRecordService cellStationRecordService;

    @RequestMapping(value = "/findByDate", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findByDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam boolean unReplyOnly
    ) throws IOException {
        return new DataTableResponse(cellStationRecordService.findByDate(startDate, endDate, unReplyOnly));
    }
}
