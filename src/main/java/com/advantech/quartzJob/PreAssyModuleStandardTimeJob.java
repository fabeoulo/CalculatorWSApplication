/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.model.db1.PreAssyModuleStandardTimeHistory;
import com.advantech.service.db1.PreAssyModuleStandardTimeHistoryService;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import com.advantech.service.db1.SystemReportService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
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
    private PreAssyModuleStandardTimeHistoryService preAssyModuleStandardTimeHistoryService;

    @Autowired
    private SystemReportService systemReportService;

    private final String _MAPKEY_OPTIME = "sumOpTime";
    private final String _MAPKEY_PCS = "sumPcs";
//    private final Integer _TOKEN_UPDATE = 1;//PreAssyModuleStandardTimeStatus.UPDATED.token();
//    private final Integer _TOKEN_NOUPDATE = 0;// PreAssyModuleStandardTimeStatus.NOUPDATE.token();

    public void execute() throws Exception {
        DateTime thisMonday = new DateTime().withDayOfWeek(DateTimeConstants.MONDAY);
        String eds = thisMonday.toString("yyyy-MM-dd");
        String sds = thisMonday.minusWeeks(1).toString("yyyy-MM-dd");

        List<Map> data = systemReportService.getBabPreAssyDetailForExcel(-1, -1, sds, eds);
        Map<String, Map> mapDetailGroup = getPreAssyStandardTime(data);

        List<PreAssyModuleStandardTime> ls = preAssyModuleStandardTimeService.findAllWithTypes();
//        resetUpdateFlag(ls);

        List<PreAssyModuleStandardTime> toUpdate = ls.stream()
                .filter(p -> {
                    if (!p.getPreAssyModuleType().getName().startsWith("(前置")) {
                        return false;
                    }

                    int totalOpTime = p.getTotalOpTime() == null ? 0 : p.getTotalOpTime();
                    int totalPcs = p.getTotalPcs() == null ? 0 : p.getTotalPcs();
                    String key = p.getModelName() + "_"
                            + p.getPreAssyModuleType().getName() + "_"
                            + p.getPreAssyModuleType().getLineType().getName();

                    if (mapDetailGroup.containsKey(key)) {
                        Map mapValue = mapDetailGroup.get(key);

                        int newTotalOpTime = totalOpTime + (int) mapValue.get(_MAPKEY_OPTIME);
                        int newTotalPcs = totalPcs + (int) mapValue.get(_MAPKEY_PCS);
                        BigDecimal avg = new BigDecimal(newTotalOpTime)
                                .divide(new BigDecimal(newTotalPcs), 1, RoundingMode.HALF_UP);

                        p.setTotalOpTime(newTotalOpTime);
                        p.setTotalPcs(newTotalPcs);
                        p.setStandardTime(avg);
//                        p.setStUpdateFlag(_TOKEN_UPDATE);

                        return true;
                    }

                    return false;
                })
                .collect(Collectors.toList());

        logHistory(toUpdate);
        preAssyModuleStandardTimeService.update(toUpdate);
    }

    private void logHistory(List<PreAssyModuleStandardTime> toUpdate) {
        toUpdate.forEach(i -> {
            PreAssyModuleStandardTimeHistory data = new PreAssyModuleStandardTimeHistory(
                    i,
                    i.getStandardTime(),
                    i.getTotalPcs(),
                    i.getTotalOpTime()
            );

            preAssyModuleStandardTimeHistoryService.insert(data);
        });
    }

//    private void resetUpdateFlag(List<PreAssyModuleStandardTime> all) {
//        all.forEach(i -> i.setStUpdateFlag(_TOKEN_NOUPDATE));
//        preAssyModuleStandardTimeService.update(all);
//    }
    private Map<String, Map> getPreAssyStandardTime(List<Map> data) {
        Map<String, Map> mapSt = new HashMap<>();

        data.stream().filter(m
                -> m.get("modelName") != null
                && m.get("preModuleName") != null
                && (int) m.get("pcs") > 0
                && (int) m.get("時間花費") > 0
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

                    Map<String, Integer> mapValue = new HashMap<>();
                    mapValue.put(_MAPKEY_PCS, pcs);
                    mapValue.put(_MAPKEY_OPTIME, spend);
                    mapSt.put(key, mapValue);
                });
        return mapSt;
    }
}
