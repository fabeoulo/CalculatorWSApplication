/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import com.advantech.service.db1.SystemReportService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class PreAssyModuleStandardTimeJob {

    @Autowired
    private PreAssyModuleStandardTimeService preAssyModuleStandardTimeService;

    @Autowired
    private SystemReportService systemReportService;

    public void execute() throws Exception {
        String sds = new DateTime().minusMonths(1).toString("yyyy-MM-dd");
        String eds = new DateTime().toString("yyyy-MM-dd");
        List<Map> data = systemReportService.getBabPreAssyDetailForExcel(-1, -1, sds, eds);

        List<PreAssyModuleStandardTime> ls = preAssyModuleStandardTimeService.findAllWithTypes();

        Map<String, BigDecimal> mapM3WtInExcel = getPreAssyStandardTime(data, Arrays.asList("5", "6"));
        Map<String, BigDecimal> mapM6WtInExcel = getPreAssyStandardTime(data, Arrays.asList("7"));
        List<String> m3Linetype = Arrays.asList("ASSY");
        List<String> m6Linetype = Arrays.asList("Cell");
        ls = ls.stream()
                .filter(p -> {
                    if (!p.getPreAssyModuleType().getName().startsWith("(前置")) {
                        return false;
                    }
                    String key = p.getModelName() + "_" + p.getPreAssyModuleType().getName();
                    String moduleLinetype = p.getPreAssyModuleType().getLineType().getName();

                    if (m3Linetype.contains(moduleLinetype) && mapM3WtInExcel.containsKey(key)) {
                        p.setStandardTime(mapM3WtInExcel.get(key));
                        return true;
                    } else if (m6Linetype.contains(moduleLinetype) && mapM6WtInExcel.containsKey(key)) {
                        p.setStandardTime(mapM6WtInExcel.get(key));
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());
        
        preAssyModuleStandardTimeService.update(ls);
    }

    private Map<String, BigDecimal> getPreAssyStandardTime(List<Map> data, List<String> floorName) {
        Map<String, BigDecimal> mapSt = new HashMap<>();
        data.stream().filter(m
                -> floorName.contains(m.get("sitefloor").toString())
                && m.get("modelName") != null
                && m.get("preModuleName") != null
        )
                .collect(Collectors.groupingBy(map -> map.get("modelName").toString() + "_" + map.get("preModuleName").toString()))
                .forEach((key, value) -> {
                    int pcs = value.stream().mapToInt(v -> (int) v.get("pcs")).sum();
                    int spend = value.stream().mapToInt(v -> (int) v.get("時間花費")).sum();
                    BigDecimal swt = new BigDecimal(spend / pcs);
                    mapSt.put(key, swt);
                });
        return mapSt;
    }
}
