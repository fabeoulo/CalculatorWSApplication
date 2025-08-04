/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.model.db1.PackingPassStationDetail;
import com.advantech.service.db1.BabService;
import com.advantech.service.db1.PackingPassStationDetailService;
import com.advantech.webservice.Factory;
import com.advantech.webservice.mes.Section;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 *
 */
@Component
public class SyncPackingPassStationData {

    private static final Logger logger = LoggerFactory.getLogger(SyncPackingPassStationData.class);

    @Autowired
    private WebServiceRV rv;

    @Autowired
    private BabService babService;

    @Autowired
    private PackingPassStationDetailService packingPassStationDetailService;

    private final List<Integer> stations = newArrayList(28);
    private DateTime sD, eD;
    private final Factory factory = Factory.TWM3;
    private final int _floorId = 6;

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
        List<Bab> babs = babService.findByDateAndStation(sD, eD, 3, -1)
                .stream().filter(bb -> bb.getIspre() == 0).collect(Collectors.toList());
        List<String> jobnumbers = findJobnumberForQuery(babs);
        if (jobnumbers.isEmpty()) {
            return;
        }

        List<PackingPassStationDetail> remoteData = findRvPassStationDetails(jobnumbers, factory);
        List<PackingPassStationDetail> remoteDataFiltered = this.filterRemoteDataByBab(remoteData, babs);
        if (remoteDataFiltered.isEmpty()) {
            return;
        }

        List<PackingPassStationDetail> dbData = packingPassStationDetailService.findByDate(sD, eD);
        syncPackingPassStationDetail(dbData, remoteDataFiltered);
    }

    private List<String> findJobnumberForQuery(List<Bab> babs) {
        Set<BabSettingHistory> settings = babs.stream().flatMap(bb -> bb.getBabSettingHistorys().stream()).collect(Collectors.toSet());
        List<String> jobnumbers = settings.stream().map(t -> "'" + t.getJobnumber() + "'").distinct().collect(Collectors.toList());
        return jobnumbers;
    }

    private List<PackingPassStationDetail> findRvPassStationDetails(List<String> jobnumbers, Factory factory) {
        List<PackingPassStationDetail> remoteData = new ArrayList();
        stations.forEach(s -> {
            Section section = Section.PACKAGE;
            List<PackingPassStationDetail> l = rv.getPackingPassStationDetails(jobnumbers, section, s, sD, eD, factory);
            remoteData.addAll(l);
        });
        return remoteData;
    }

    private List<PackingPassStationDetail> filterRemoteDataByBab(List<PackingPassStationDetail> result, List<Bab> babs) {

        Map<String, List<Bab>> poBabMap = babs.stream().collect(Collectors.groupingBy(Bab::getPo));

        List<PackingPassStationDetail> resultFilter = result.stream()
                .filter(r -> poBabMap.keySet().contains(r.getPo()))
                .map(r -> {
                    List<Bab> poBabs = poBabMap.get(r.getPo());
                    Bab matchBab = poBabs.stream()
                            .filter(bb -> {
                                return bb.getBabSettingHistorys().stream().anyMatch(bsh -> bsh.getJobnumber().equals(r.getJobnumber()))
                                        && new DateTime(bb.getLastUpdateTime()).compareTo(new DateTime(r.getCreateDate())) >= 0;
                            })
                            .max(Comparator.comparingInt(Bab::getId)).orElse(null);

                    r.setBab(matchBab);
                    return r;
                })
                .collect(Collectors.toList());
        return resultFilter;
    }

    private void syncPackingPassStationDetail(List<PackingPassStationDetail> dbData, List<PackingPassStationDetail> remoteData) {
        List<PackingPassStationDetail> delData = (List<PackingPassStationDetail>) CollectionUtils.subtract(dbData, remoteData);
        packingPassStationDetailService.delete(delData);
        logger.info("Delete data cnt " + delData.size());

        List<PackingPassStationDetail> newData = (List<PackingPassStationDetail>) CollectionUtils.subtract(remoteData, dbData);
        newData = getFixInsertOrder(newData); // fix order for calculate PassStationProductivity
        packingPassStationDetailService.insert(newData);
        logger.info("New data cnt " + newData.size());
    }

    private List<PackingPassStationDetail> getFixInsertOrder(List<PackingPassStationDetail> l) {
        return l.stream().sorted(
                Comparator.comparing(PackingPassStationDetail::getJobnumber)
                        .thenComparing(PackingPassStationDetail::getCreateDate))
                .collect(Collectors.toList());
    }
}
