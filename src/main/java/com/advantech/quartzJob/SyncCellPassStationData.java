/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.CellPassStationDetail;
import com.advantech.model.db1.CellStationRecord;
import com.advantech.service.db1.CellPassStationDetailService;
import com.advantech.service.db1.CellStationRecordService;
import com.advantech.webservice.Factory;
import com.advantech.webservice.mes.Section;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jusitn.Yeh
 *
 */
@Component
public class SyncCellPassStationData {

    private static final Logger logger = LoggerFactory.getLogger(SyncCellPassStationData.class);

    @Autowired
    private WebServiceRV rv;

    @Autowired
    private CellStationRecordService cellStationRecordService;

    @Autowired
    private CellPassStationDetailService cellPassStationDetailService;

    private DateTime sD, eD;
    private final List<Integer> stations = newArrayList(159, 182, 140); // SL,SL1,ASSY1

    public void setsD(DateTime sD) {
        this.sD = sD;
    }

    public void seteD(DateTime eD) {
        this.eD = eD;
    }

    public void execute() {
        DateTime today = new DateTime();

//        DateTime sD = new DateTime(today).minusDays(today.getDayOfWeek() == 1 ? 3 : 1).withTime(8, 0, 0, 0);
//        DateTime eD = new DateTime(today).withTime(8, 0, 0, 0);
//
        int hr = today.getHourOfDay() >= 20 ? 20 : 8;
        eD = new DateTime(today).withTime(hr, 30, 0, 0);
        sD = eD.minusHours(12);

        syncPassStationDetail();
    }

    public void syncPassStationDetail() {
        List<CellPassStationDetail> remoteData = new ArrayList();

        List<String> jobnumbers = findJobnumberForQuery();
        if (jobnumbers.isEmpty()) {
            return;
        }

        findRvPassStationDetails(remoteData, jobnumbers);
        if (remoteData.isEmpty()) {
            return;
        }

        List<CellPassStationDetail> dbData = cellPassStationDetailService.findByDate(sD, eD);
        syncCellPassStationDetail(dbData, remoteData);
    }

    private List<String> findJobnumberForQuery() {
        List<CellStationRecord> records = cellStationRecordService.findByDate(sD, eD, false);
        List<String> jobnumbers = records.stream().map(t -> "'" + t.getUserId() + "'").distinct().collect(Collectors.toList());
        return jobnumbers;
    }

    private List<CellPassStationDetail> findRvPassStationDetails(List<CellPassStationDetail> result, List<String> jobnumbers) {
        stations.forEach(s -> {
            Section section = (s == 3 ? Section.BAB : Section.TEST);
            List<CellPassStationDetail> l = rv.getCellPassStationDetails(jobnumbers, section, s, sD, eD, Factory.TWM6);
//            List<CellPassStationDetail> l2 = rv.getCellPassStationDetails(jobnumbers, section, s, sD, eD, Factory.TWM6);
            result.addAll(l);
//            result.addAll(l2);
        });
        return result;
    }

    private void syncCellPassStationDetail(List<CellPassStationDetail> dbData, List<CellPassStationDetail> remoteData) {
        List<CellPassStationDetail> delData = (List<CellPassStationDetail>) CollectionUtils.subtract(dbData, remoteData);
        cellPassStationDetailService.delete(delData);
        logger.info("Delete data cnt " + delData.size());

        List<CellPassStationDetail> newData = (List<CellPassStationDetail>) CollectionUtils.subtract(remoteData, dbData);
        newData = getFixInsertOrder(newData); // fix order for calculate PassStationProductivity
        cellPassStationDetailService.insert(newData);
        logger.info("New data cnt " + newData.size());
    }

    private List<CellPassStationDetail> getFixInsertOrder(List<CellPassStationDetail> l) {
        return l.stream().sorted(
                Comparator.comparing(CellPassStationDetail::getJobnumber)
                        .thenComparing(CellPassStationDetail::getCreateDate))
                .collect(Collectors.toList());
    }
}
