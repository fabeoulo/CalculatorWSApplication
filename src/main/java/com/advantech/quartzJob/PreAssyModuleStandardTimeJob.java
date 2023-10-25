/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import com.advantech.service.db1.SystemReportService;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Map<String, BigDecimal> mapWtInExcel = getPreAssyStandardTime(data);

        ls = ls.stream()
                .filter(p -> {
                    if (!p.getPreAssyModuleType().getName().startsWith("(前置")) {
                        return false;
                    }

                    String key = p.getModelName() + "_"
                            + p.getPreAssyModuleType().getName() + "_"
                            + p.getPreAssyModuleType().getLineType().getName();
                    if (mapWtInExcel.containsKey(key)) {
                        BigDecimal newST = mapWtInExcel.get(key);
                        BigDecimal avg = newST.add(p.getStandardTime()).divide(new BigDecimal(2), 1, RoundingMode.HALF_UP);
                        p.setStandardTime(avg);
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

        preAssyModuleStandardTimeService.update(ls);
    }

    private Map<String, BigDecimal> getPreAssyStandardTime(List<Map> data) {
        Map<String, BigDecimal> mapSt = new HashMap<>();
        data.stream().filter(m
                -> m.get("modelName") != null
                && m.get("preModuleName") != null
        )
                .collect(Collectors.groupingBy(
                        map
                        -> map.get("modelName").toString() + "_"
                        + map.get("preModuleName").toString() + "_"
                        + map.get("lineType").toString()
                ))
                .forEach((key, value) -> {
                    int pcs = value.stream().mapToInt(v -> (int) v.get("pcs")).sum();
                    int spend = value.stream().mapToInt(v -> (int) v.get("時間花費")).sum();
                    BigDecimal swt = new BigDecimal(spend).divide(new BigDecimal(pcs), 1, RoundingMode.HALF_UP);
                    mapSt.put(key, swt);
                });
        return mapSt;
    }
}
