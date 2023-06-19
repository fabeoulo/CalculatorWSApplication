/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import com.advantech.service.db1.SystemReportService;
import java.math.BigDecimal;
import java.util.ArrayList;
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

        Map<String, BigDecimal> mapM3St = getPreAssyStandardTime(data, "CLEAN PANEL", Arrays.asList("5", "6"));
        Map<String, BigDecimal> mapM6St = getPreAssyStandardTime(data, "CLEAN PANEL", Arrays.asList("7"));

        List<String> modelNames = new ArrayList<>();
        modelNames.addAll(mapM3St.keySet());
        modelNames.addAll(mapM6St.keySet());

        List<Integer> typeIds = Arrays.asList(44, 322);
        List<PreAssyModuleStandardTime> ls = preAssyModuleStandardTimeService.findAll();
        ls = ls.stream().filter(t
                -> modelNames.contains(t.getModelName())
                && typeIds.contains(t.getPreAssyModuleType().getId())
        ).collect(Collectors.toList());

        ls.forEach(e -> {
            if (e.getPreAssyModuleType().getId() == 44 && mapM3St.containsKey(e.getModelName())) {
                BigDecimal newST = mapM3St.get(e.getModelName());
                e.setStandardTime(newST);
            } else if (e.getPreAssyModuleType().getId() == 322 && mapM6St.containsKey(e.getModelName())) {
                BigDecimal newST = mapM6St.get(e.getModelName());
                e.setStandardTime(newST);
            }
        });
        preAssyModuleStandardTimeService.update(ls);
    }
    
    private Map<String, BigDecimal> getPreAssyStandardTime(List<Map> data, String moduleName, List<String> floorIdstring) {
        Map<String, BigDecimal> mapSt = new HashMap<>();
        data.stream().filter(m
                -> floorIdstring.contains(m.get("sitefloor").toString())
                && m.get("preModuleName") != null
                && m.get("preModuleName").toString().contains(moduleName)
        )
                .collect(Collectors.groupingBy(map -> map.get("modelName").toString()))
                .forEach((key, value) -> {
                    int pcs = value.stream().mapToInt(v -> (int) v.get("pcs")).sum();
                    int spend = value.stream().mapToInt(v -> (int) v.get("時間花費")).sum();
                    BigDecimal swt = new BigDecimal(spend / pcs);
                    mapSt.put(key, swt);
                });
        return mapSt;
    }
}
