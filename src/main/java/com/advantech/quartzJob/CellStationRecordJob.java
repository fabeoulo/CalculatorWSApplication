/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 專案的核心，控制排程的工作要做些什麼
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.CellLoginRecord;
import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.PropertiesReader;
import com.advantech.model.db1.CellStationRecord;
import com.advantech.model.db1.LineType;
import com.advantech.model.db1.LineTypeConfig;
import com.advantech.model.db1.ReplyStatus;
import com.advantech.service.db1.LineTypeConfigService;
import com.advantech.service.db1.LineTypeService;
import com.advantech.service.db1.CellStationRecordService;
import com.advantech.service.db1.CellLoginRecordService;
import com.advantech.webservice.Factory;
import com.advantech.webservice.WebServiceRV;
import static com.google.common.base.Preconditions.checkState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.joda.time.DateTime;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 *
 * @author Jusitn.Yeh
 */
public class CellStationRecordJob extends QuartzJobBean {

    private static final Logger log = LoggerFactory.getLogger(CellStationRecordJob.class);
    private final Set<Integer> EXCLUDE_HOUR_SET = new HashSet<>(Arrays.asList(8, 12, 20, 0));

    private final CellLoginRecordService cellLoginRecordService;
    private final CellStationRecordService cellStationRecordService;
    private final LineTypeService lineTypeService;
    private final LineTypeConfigService lineTypeConfigService;
//    private final WebServiceRV rv;
//    private final PropertiesReader p;

    private Double minProductivity, maxProductivity;

    public CellStationRecordJob() {
        cellLoginRecordService = (CellLoginRecordService) ApplicationContextHelper.getBean("cellLoginRecordService");
        cellStationRecordService = (CellStationRecordService) ApplicationContextHelper.getBean("cellStationRecordService");
//        rv = (WebServiceRV) ApplicationContextHelper.getBean("webServiceRV");
        lineTypeService = (LineTypeService) ApplicationContextHelper.getBean("lineTypeService");
        lineTypeConfigService = (LineTypeConfigService) ApplicationContextHelper.getBean("lineTypeConfigService");
//        p = (PropertiesReader) ApplicationContextHelper.getBean("propertiesReader");
    }

    @Override
    public void executeInternal(JobExecutionContext jec) throws JobExecutionException {
        DateTime d = new DateTime();
        log.info("It's " + d.toString() + " right now, begin record the cellStationRecord...");
        if (EXCLUDE_HOUR_SET.contains(d.getHourOfDay())) {
            log.info("No need to record right now.");
        } else {
            //只存下已經刷入的使用者
            List<CellStationRecord> cellStationStatus = separateOfflineUser();

//            updateReplyFlag(cellStationStatus);
            cellStationRecordService.insert(cellStationStatus);
            log.info("Record success");
        }
    }

    private List<CellStationRecord> separateOfflineUser() {
        List<CellLoginRecord> records = cellLoginRecordService.findAll();
        List list = new ArrayList();
        Date d = new Date();

        records.stream().forEachOrdered((clr) -> {
            CellStationRecord csr = new CellStationRecord();
            csr.setUserId(clr.getJobnumber());
            csr.setUserName(clr.getUserName());
            csr.setLastUpdateTime(d);
            csr.setCellStation(clr.getCellStation());
            list.add(csr);
        });

        return list;
    }

    private void updateReplyFlag(List<com.advantech.model.db1.TestRecord> l) {
        initProductivityStandard();
        l.forEach((rec) -> {
            Double productivity = rec.getProductivity();
            rec.setReplyStatus(productivity > maxProductivity || productivity < minProductivity
                    ? ReplyStatus.UNREPLIED : ReplyStatus.NO_NEED_TO_REPLY);
        });
    }

    private void initProductivityStandard() {
        LineType lineType = lineTypeService.findByName("Cell");
        checkState(lineType != null, "Can't find lineType name Cell.");
        List<LineTypeConfig> config = lineTypeConfigService.findByLineType(lineType.getId());
        LineTypeConfig minConf = config.stream().filter(s -> "PRODUCTIVITY_STANDARD_MIN".equals(s.getName())).findFirst().orElse(null);
        checkState(minConf.getValue() != null, "Can't find PRODUCTIVITY_STANDARD_MIN setting in lineTypeConfig.");
        this.minProductivity = minConf.getValue().doubleValue();
        LineTypeConfig maxConf = config.stream().filter(s -> "PRODUCTIVITY_STANDARD_MAX".equals(s.getName())).findFirst().orElse(null);
        checkState(maxConf.getValue() != null, "Can't find PRODUCTIVITY_STANDARD_MAX setting in lineTypeConfig.");
        this.maxProductivity = maxConf.getValue().doubleValue();
    }

}
