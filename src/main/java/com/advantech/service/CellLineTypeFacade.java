/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.entity.AlarmAction;
import com.advantech.entity.CellLine;
import com.advantech.helper.PropertiesReader;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class CellLineTypeFacade extends BasicLineTypeFacade {

    private static final Logger log = LoggerFactory.getLogger(CellLineTypeFacade.class);

    private static CellLineTypeFacade instance;
    private final CellService cellService;
    private final Double cellStandardMin, cellStandardMax;

    private CellLineTypeFacade() {
        cellService = BasicService.getCellService();
        PropertiesReader p = PropertiesReader.getInstance();
        cellStandardMin = p.getCellStandardMin();
        cellStandardMax = p.getCellStandardMax();
        this.init();
    }

    public static CellLineTypeFacade getInstance() {
        if (instance == null) {
            instance = new CellLineTypeFacade();
        }
        return instance;
    }

    private void init() {
        this.initMap();
        if (isWriteToDB) {
            boolean initStatus = this.initDbAlarmSign();
            if (initStatus == false) {
                log.error("Init db output fail.");
            }
        }
    }

    @Override
    protected void initMap() {
        this.dataMap.clear();
        List<CellLine> l = BasicService.getCellLineService().findAll();
        for (CellLine cellLine : l) {
            dataMap.put(cellLine.getOutputName(), super.NORMAL_SIGN);
        }
    }

    @Override
    protected boolean generateData() {
        List<Map> l = BasicService.getPassStationService().getCellLastGroupStatusView();
        boolean isCellsUnderBalance = false;
        this.initMap();
        if (!l.isEmpty()) {
            processingJsonObject = new JSONObject();
            String percentKeyName = "percent";
            String lineNameKeyName = "linename";
            String outputLineNameKeyName = "outputLinename";
            for (Map m : l) {
                if (m.containsKey(percentKeyName) && m.containsKey(lineNameKeyName) && m.containsKey(outputLineNameKeyName)) {
                    String outputLineName = (String) m.get(outputLineNameKeyName);
                    Double percent = (Double) m.get(percentKeyName);

                    boolean isPass = isInTheRange(percent);
                    if (isPass) {
                        dataMap.put(outputLineName, this.NORMAL_SIGN);
                    } else {
                        dataMap.put(outputLineName, this.ALARM_SIGN);
                        isCellsUnderBalance = true;
                    }
                    m.put("isAlarm", !isPass);
                } else {
                    log.error("Can not find the spec key in map, need keys " + percentKeyName + " " + lineNameKeyName + " " + outputLineNameKeyName);
                    return false;
                }
            }
            processingJsonObject.put("data", l);
        } else {
            processingJsonObject = null;
        }
        return isCellsUnderBalance;
    }

    private boolean isInTheRange(Double percent) {
        return percent >= this.cellStandardMin && percent <= this.cellStandardMax;
    }

    @Override
    protected boolean initDbAlarmSign() {
        return cellService.removeAlarmSign() && cellService.insertAlarm(super.mapToAlarmSign(dataMap));
    }

    @Override
    public boolean setDbAlarmSignToTestMode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean setDbAlarmSign(List<AlarmAction> l) {
        return cellService.updateAlarm(l);
    }

    @Override
    protected boolean resetDbAlarmSign() {
        return cellService.resetAlarm();
    }

}