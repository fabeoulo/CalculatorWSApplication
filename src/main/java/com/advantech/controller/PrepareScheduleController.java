/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.datatable.DataTableResponse;
import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.LineType;
import com.advantech.model.db1.PrepareSchedule;
import com.advantech.model.db1.RptStationQty;
import com.advantech.model.db1.User;
import com.advantech.quartzJob.ArrangePrepareScheduleImpl_1;
import com.advantech.service.db1.FloorService;
import com.advantech.service.db1.LineTypeService;
import com.advantech.service.db1.PrepareScheduleService;
import com.advantech.webservice.Factory;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.base.Preconditions.checkState;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping(value = "/PrepareScheduleController")
public class PrepareScheduleController {

    @Autowired
    private PrepareScheduleService wService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private ArrangePrepareScheduleImpl_1 aps1;

    @Autowired
    private PrepareScheduleService psService;

    @Autowired
    private LineTypeService lineTypeService;

    @Autowired
    private WebServiceRV rv;

    @RequestMapping(value = "/findPrepareSchedule", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findPrepareSchedule(
            @RequestParam(required = false) Integer floorId,
            @RequestParam("lineType_id[]") Integer[] lineType_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime d,
            HttpServletRequest request
    ) {

        Floor f;

        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_OPER_IE") && floorId != null) {
            f = floorService.findByPrimaryKey(floorId);
        } else {
            User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
            f = user.getFloor();
        }

//        List l = aps1.findPrepareSchedule(f, d);
        List l = aps1.findPrepareSchedule(f, lineType_id, d);
        return new DataTableResponse(l);
    }

    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    @ResponseBody
    protected String update(
            @ModelAttribute PrepareSchedule pojo
    ) {
        wService.updateAndResortPriority(pojo);
        return "success";
    }

    @RequestMapping(value = "/splitCnt", method = {RequestMethod.POST})
    @ResponseBody
    protected String splitCnt(
            @RequestParam int id,
            @RequestParam List<Integer> cnt
    ) {

        PrepareSchedule pojo = wService.findByPrimaryKey(id);
        checkState(pojo != null, "Can't find po in schedule " + id);
        checkState(isInCurrentDate(pojo.getOnBoardDate()), "Onboard date invalid " + id);

        wService.separateCnt(pojo, cnt);

        return "success";

    }

    private boolean isInCurrentDate(Date d1) {
        DateTime dd1 = new DateTime(d1).withTime(0, 0, 0, 0);
        DateTime dd2 = new DateTime().withTime(0, 0, 0, 0);
        return dd1.isEqual(dd2);
    }

    @RequestMapping(value = "/findPrepareSchedulePercentage", method = {RequestMethod.GET})
    @ResponseBody
    protected DataTableResponse findPrepareSchedulePercentage(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam("lineType_id[]") Integer[] lineType_id,
            HttpServletRequest request
    ) {
        List<LineType> lineTypes = lineTypeService.findByPrimaryKeys(lineType_id);

        List<PrepareSchedule> schedules = psService.findByLineTypeAndDate(lineTypes, startDate);
        List<RptStationQty> mesQty = rv.getRptStationQtys(startDate.minusDays(7), startDate.plusDays(7), Factory.DEFAULT);

        schedules.stream().forEach(p -> {
            int rptStationQty = mesQty.stream()
                    .filter(m -> m.getPo().equals(p.getPo()))
                    .mapToInt(m -> m.getQty()).sum();

            Map otherInfo = new HashMap();
            otherInfo.put("passCntQry", rptStationQty);
            p.setOtherInfo(otherInfo);

        });

        return new DataTableResponse(schedules);
    }

}
