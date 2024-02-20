/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.facade;

import com.advantech.helper.PropertiesReader;
import com.advantech.model.db1.AlarmDO;
import com.advantech.service.db1.AlarmDOService;
import com.advantech.webapi.WaGetTagValue;
import com.advantech.webapi.WaSetTagValue;
import com.advantech.webapi.model.WaTagNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Wei.Cheng
 */
@Component
public abstract class BasicLineTypeFacade implements com.advantech.service.db1.AlarmAction {

    private static final Logger log = LoggerFactory.getLogger(BasicLineTypeFacade.class);

    protected Boolean controlJobFlag = true;//Change the flag if you want to pause the job outside.

    protected Boolean isOutputSomewhere;

    protected final int ALARM_SIGN = 1, NORMAL_SIGN = 0;

    protected Boolean resetFlag;//設定Flag，以免被重複init，當True才reset.

    protected Map dataMap;//占存資料用map
    protected JSONObject processingJsonObject;//暫存處理過的資料

    protected Boolean isNeedToOutputResult;//從改寫的function 得知是否要output(有人亮燈時).

    @Autowired
    private PropertiesReader p;

    @Autowired
    private AlarmDOService alarmDOService;

    @Autowired
    private WaGetTagValue waGetTagValue;

    @Autowired
    protected WaSetTagValue waSetTagValue;

    @PostConstruct
    protected void initValues() {
        log.info("BasicLineTypeFacade init");
        isOutputSomewhere = p.getIsCalculateResultOutput();
        resetFlag = true;
        dataMap = new HashMap();
    }

    public void processingDataAndSave() {
        if (controlJobFlag == true) {
            isNeedToOutputResult = this.generateData();
            if (isNeedToOutputResult) {
                outputResult(dataMap);
            } else {
                resetOutputResult();
            }
        }
    }

    /**
     * Init the super.dataMap first.
     */
    protected abstract void initMap();

    /**
     * Generate data and put the data into variable processingJsonObject.
     *
     * @return Someone is under the balance or not.
     */
    protected abstract boolean generateData();

    private void outputResult(Map m) {
        if (isOutputSomewhere) {
            outputAlarmSign(m);
            resetFlag = true;
        }
    }

    protected void resetOutputResult() {
        if (isOutputSomewhere) {
            if (resetFlag == true) {
                initMap();
                resetFlag = false;
            }
            resetAlarmSign();
        }
    }

    private void outputAlarmSign(Map map) {
        setAlarmSign(mapToAlarmSign(map));
    }

    protected abstract List<com.advantech.model.db1.AlarmAction> mapToAlarmSign(Map map);

    public void resetAlarm() throws IOException {
        if (isOutputSomewhere) {
            resetAlarmSign();
        }
        initInnerObjs();
    }

    protected boolean hasDataInCollection(Collection c) {
        return c != null && !c.isEmpty();
    }

    public void initInnerObjs() {
        dataMap.clear();
        this.processingJsonObject = null;
    }

    /**
     * This JSONObject is already DataTable form.
     *
     * @return
     */
    public JSONObject getJSONObject() {
        return this.processingJsonObject;
    }

    public Map getMap() {
        return this.dataMap;
    }

    public void isNeedToOutput(boolean controlJobFlag) {
        this.controlJobFlag = controlJobFlag;
    }

    protected <T extends com.advantech.model.db1.AlarmAction> void setAlarmSignWa(List<T> alarmActions) {
        if (alarmActions != null) {
            //find DO by TableIds & active DOs        
            Map<String, String> mapTableIdToDO = this.findFilterMapByTables(alarmActions);

            //set requestBody
            List<WaTagNode> requestModels = new ArrayList<>();
            alarmActions.forEach(e -> {
                if (mapTableIdToDO.containsKey(e.getTableId())) {
                    requestModels.add(
                            new WaTagNode(mapTableIdToDO.get(e.getTableId()), e.getAlarm())
                    );
                }
            });

            waSetTagValue.exchange(requestModels);
        }
    }

    protected void resetAlarmSignWa(List alarmActions) {
        //find DO by TableIds & active DOs        
        Map<String, String> mapTableIdToDO = this.findFilterMapByTables(alarmActions);

        //set requestBody
        List<WaTagNode> requestModels = new ArrayList<>();
        mapTableIdToDO.entrySet().forEach((entry)
                -> requestModels.add(new WaTagNode(entry.getValue(), 0)));

        //reset
        waSetTagValue.exchange(requestModels);
    }

    private <T extends com.advantech.model.db1.AlarmAction> Map<String, String> findFilterMapByTables(List<T> alarmActions) {
        //find table ID
        List<String> tableIds = alarmActions.stream()
                .map(e -> e.getTableId())
                .collect(Collectors.toList());

        //find active DO
        Map allActiveTags = waGetTagValue.getMap();
        List<String> liveDOs = new ArrayList<>(allActiveTags.keySet());

        List<AlarmDO> f_alarmDOs = alarmDOService.findAllByTablesAndDOs(tableIds, liveDOs);
        return f_alarmDOs.stream()
                .collect(Collectors.toMap(AlarmDO::getProcessName, AlarmDO::getCorrespondDO));
    }
}
