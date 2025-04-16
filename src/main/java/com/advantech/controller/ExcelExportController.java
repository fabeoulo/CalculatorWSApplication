/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.helper.ExcelGenerator;
import com.advantech.service.db1.SystemReportService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("ExcelExportController")
public class ExcelExportController {

    @Autowired
    private SystemReportService reportService;

    private final int minAllowAmount = 10;

    private DateTimeFormatter fmt;

    @PostConstruct
    protected void init() {
        fmt = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/getTotalInfoReport", method = {RequestMethod.GET})
    @ResponseBody
    protected void getTotalInfoReport(
            @RequestParam int lineType_id,
            @RequestParam int floor_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam boolean aboveStandard,
            HttpServletResponse res) throws IOException {

        startDate = startDate.withHourOfDay(0);
        endDate = endDate.withHourOfDay(23);

        String sD = fmt.print(startDate);
        String eD = fmt.print(endDate);

        //http://stackoverflow.com/questions/13853300/jquery-file-download-filedownload
        //personalAlm記得轉格式才能special Excel generate
        List<Map> countermeasures = reportService.getCountermeasureForExcel(lineType_id, floor_id, sD, eD, aboveStandard);
        List<Map> personalAlarms = reportService.getPersonalAlmForExcel(lineType_id, floor_id, sD, eD, aboveStandard);
        List<Map> emptyRecords = reportService.getEmptyRecordForExcel(lineType_id, floor_id, sD, eD);

        List list = countermeasures;
        List list2 = transformPersonalAlmDataPattern(personalAlarms);//把各站亮燈頻率合併為橫式(類似 sql 的 Group by格式)
        List list3 = emptyRecords;

        if (list.isEmpty() || (list.isEmpty() && list2.isEmpty())) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            try ( Workbook w = this.generateBabDetailIntoExcel(list, list2, list3, aboveStandard)) {
                String fileExt = ExcelGenerator.getFileExt(w);

                res.setContentType("application/vnd.ms-excel");
                res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                res.setHeader("Content-Disposition",
                        "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                w.write(res.getOutputStream());
            }
        }
    }

    @RequestMapping(value = "/getBabPassStationExceptionReportDetails", method = {RequestMethod.GET})
    @ResponseBody
    protected void getBabPassStationExceptionReportDetails(
            @RequestParam String po,
            @RequestParam String modelName,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam int lineType_id,
            HttpServletResponse res) throws IOException {

        startDate = startDate.withHourOfDay(0);
        endDate = endDate.withHourOfDay(23);

        String sD = fmt.print(startDate);
        String eD = fmt.print(endDate);

        List<Map> data = reportService.getBabPassStationExceptionReportDetails(po, modelName, sD, eD, lineType_id);
        quickGenerateExcel(data, res);
    }

    @RequestMapping(value = "/getBabPreAssyDetail", method = {RequestMethod.GET})
    @ResponseBody
    protected void getBabPreAssyDetail(
            @RequestParam int lineType_id,
            @RequestParam int floor_id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            HttpServletResponse res) throws IOException {

        startDate = startDate.withHourOfDay(0);
        endDate = endDate.withHourOfDay(23);

        String sD = fmt.print(startDate);
        String eD = fmt.print(endDate);

        List<Map> data = reportService.getBabPreAssyDetailForExcel(lineType_id, floor_id, sD, eD);
        quickGenerateExcel(data, res);
    }

    @RequestMapping(value = "/getPreAssyModuleStandardTimeSetting", method = {RequestMethod.GET})
    @ResponseBody
    protected void getPreAssyModuleStandardTimeSetting(HttpServletResponse res) throws IOException {
        List<Map> data = reportService.getPreAssyModuleStandardTimeSetting();
        quickGenerateExcel(data, res);
    }

    @RequestMapping(value = "/getAssyModelSopStandardTimeSetting", method = {RequestMethod.GET})
    @ResponseBody
    protected void getAssyModelSopStandardTimeSetting(HttpServletResponse res) throws IOException {
        List<Map> data = reportService.getAssyModelSopStandardTimeSetting();
        quickGenerateExcel(data, res);
    }

    @RequestMapping(value = "/getTtestSuggestionWorkTimeDetailExcel", method = {RequestMethod.GET})
    @ResponseBody
    protected void getTtestSuggestionWorkTimeDetailExcel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            HttpServletResponse res) throws IOException {

        endDate = endDate.plusDays(1);

        String sD = fmt.print(startDate);
        String eD = fmt.print(endDate);

        List<Map> data = reportService.getTtestSuggestionWorkTimeDetailExcel(sD, eD);
        quickGenerateExcel(data, res, 8);
    }

    @RequestMapping(value = "/getSuggestionWorkTimeDetailExcel", method = {RequestMethod.GET})
    @ResponseBody
    protected void getSuggestionWorkTimeDetailExcel(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime endDate,
            @RequestParam int lineTypeId,
            HttpServletResponse res) throws IOException {

        endDate = endDate.plusDays(1);

        String sD = fmt.print(startDate);
        String eD = fmt.print(endDate);

        List<Map> data = reportService.getSuggestionWorkTimeDetailExcel(sD, eD, lineTypeId);
        quickGenerateExcel(data, res, 7, 9);
    }

    private void quickGenerateExcel(List<Map> data, HttpServletResponse res, Integer... i) throws IOException {
        if (data.isEmpty()) {
            res.setContentType("text/html");
            res.getWriter().println("fail");
        } else {
            ExcelGenerator generator = new ExcelGenerator();
            generator.createExcelSheet("sheet1");

            generator.addSkipDecimalFormatIndex(i);

            generator.generateWorkBooks(data);
            try ( Workbook w = generator.getWorkbook()) {
                String fileExt = ExcelGenerator.getFileExt(w);

                res.setContentType("application/vnd.ms-excel");
                res.setHeader("Set-Cookie", "fileDownload=true; path=/");
                res.setHeader("Content-Disposition",
                        "attachment; filename=sampleData" + new DatetimeGenerator("yyyyMMdd").getToday() + fileExt);
                w.write(res.getOutputStream());
            }
        }
    }

    private Workbook generateBabDetailIntoExcel(List<Map> countermeasures, List<Map> personalAlarms, List<Map> emptyRecords, boolean showAboveOnly) {

        String filterColumnName = "測量數量";
        String countermeasureSheetName = "異常填寫";
        String personalAlmSheetName = "亮燈頻率";
        String lastSheetName = "無儲存紀錄工單列表";

        List<Map> sheet1Data = new ArrayList(), sheet2Data = new ArrayList(), sheet3Data, sheet4Data = new ArrayList();
        List<Map> lastSheetData = emptyRecords;

        countermeasures.stream().filter((m) -> (m.containsKey(filterColumnName))).forEachOrdered((m) -> {
            if ((Integer) m.get(filterColumnName) >= minAllowAmount) {
                sheet1Data.add(m);
            } else {
                sheet2Data.add(m);
            }
        });

        Iterator it = personalAlarms.iterator();
        while (it.hasNext()) {
            Map personalAlarm = (Map) it.next();
            for (Map cm : sheet2Data) {
                if (Objects.equals(cm.get("id"), personalAlarm.get("id"))) {
                    sheet4Data.add(personalAlarm);
                    it.remove();
                    break;
                }
            }
        }
        sheet3Data = personalAlarms;

        ExcelGenerator generator = new ExcelGenerator();

        generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "≧" + minAllowAmount + "台)");
        generator.generateWorkBooks(sheet1Data);

        generator.createExcelSheet(personalAlmSheetName + "(" + filterColumnName + "≧" + minAllowAmount + "台)");
        generator.appendSpecialPattern(sheet3Data, 10, "Z", "瓶頸站", "AA", "亮燈頻率");

        if (!showAboveOnly && !sheet2Data.isEmpty() && !sheet4Data.isEmpty()) {
            generator.createExcelSheet(countermeasureSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.generateWorkBooks(sheet2Data);
            generator.createExcelSheet(personalAlmSheetName + "(" + filterColumnName + "<" + minAllowAmount + "台)");
            generator.appendSpecialPattern(sheet4Data, 10, "Z", "瓶頸站", "AA", "亮燈頻率");
        }

        generator.createExcelSheet(lastSheetName);
        generator.generateWorkBooks(lastSheetData);

        return generator.getWorkbook();
    }

    public List<Map> transformPersonalAlmDataPattern(List<Map> l) {
        List<Map> tList = new ArrayList();
        Map baseMap = null;
        int baseId = 0;
        String tagNameFieldName = "tagName";
        String userIdFieldName = "USER_ID";
        String stationFieldName = "station";
        String failPercentFieldName = "failPercent(Personal)";
        String idFieldName = "id";
        String failPercentFieldNameCH = "亮燈頻率";

        for (int i = 0; i < l.size(); i++) {
            Map m = l.get(i);
            if (i == 0) {
                baseMap = m;
                baseId = (int) m.get(idFieldName);
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
            } else if ((int) m.get("id") != baseId) {
                tList.add(baseMap);
                baseMap = m;
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
                baseId = (int) m.get(idFieldName);
            } else if (baseMap != null && (int) m.get(idFieldName) == baseId) {
                if ((int) m.get(stationFieldName) != 1) {
                    baseMap.replace(tagNameFieldName,
                            baseMap.get(tagNameFieldName).toString() + ", " + m.get(tagNameFieldName).toString());
                }
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                if (i == (l.size() - 1)) {
                    tList.add(baseMap);
                }
            }
        }
        return tList;
    }

    private Map removeUnusedKeyInMap(Map m, String... keys) {
        for (String st : keys) {
            m.remove(st);
        }
        return m;
    }
    //--------效率報表------------------------------

    public List<Map> transformEfficiencyReportPattern(List<Map> l) {
        List<Map> tList = new ArrayList();
        Map baseMap = null;
        int baseId = 0;
        String userIdFieldName = "USER_ID";
        String stationFieldName = "station";
        String failPercentFieldName = "failPcs";
        String idFieldName = "id";
        String failPercentFieldNameCH = "亮燈次數";

        for (int i = 0; i < l.size(); i++) {
            Map m = l.get(i);
            if (i == 0) {
                baseMap = m;
                baseId = (int) m.get(idFieldName);
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
            } else if ((int) m.get("id") != baseId) {
                tList.add(baseMap);
                baseMap = m;
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                removeUnusedKeyInMap(baseMap, userIdFieldName, stationFieldName, failPercentFieldName);
                baseId = (int) m.get(idFieldName);
            } else if (baseMap != null && (int) m.get(idFieldName) == baseId) {
                baseMap.put(userIdFieldName + m.get(stationFieldName), m.get(userIdFieldName));
                baseMap.put(failPercentFieldNameCH + m.get(stationFieldName), m.get(failPercentFieldName));
                if (i == (l.size() - 1)) {
                    tList.add(baseMap);
                }
            }
        }
        return tList;
    }

}
