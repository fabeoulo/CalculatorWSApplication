/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.facade;

import com.advantech.model.db1.AlarmDO;
import com.advantech.model.db1.AlarmTestAction;
import com.advantech.service.db1.AlarmDOService;
import com.advantech.service.db1.AlarmTestActionService;
import com.advantech.webapi.WaGetTagValue;
import com.advantech.webapi.WaSetTagValue;
import com.advantech.webapi.model.WaTagNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
@Primary
public class TestLineTypeFacade2 extends TestLineTypeFacade {

    @Autowired
    private AlarmDOService alarmDOService;

    @Autowired
    @Qualifier("alarmTestActionService")
    private AlarmTestActionService almService;
    
    @Autowired
    private WaGetTagValue waGetTagValue;

    @Autowired
    private WaSetTagValue waSetTagValue;

    @Override
    public void setAlarmSign(List l) {

        List<AlarmTestAction> alarmActions = (List<AlarmTestAction>) l;

        if (alarmActions != null) {
            //find DO
            List<AlarmDO> listDO = findDOByTables(alarmActions);

            //change list to map only including active TableIds-DOs
            Map allActiveTags = waGetTagValue.getMap();
            Map<String, String> mapTablesDOs = listDO.stream()
                    .filter(e -> allActiveTags.containsKey(e.getCorrespondDO()))
                    .collect(Collectors.toMap(AlarmDO::getProcessName, AlarmDO::getCorrespondDO));

            //set requestBody
            List<WaTagNode> requestModels = new ArrayList<>();
            alarmActions.forEach(e -> {
                if (mapTablesDOs.containsKey(e.getTableId())) {
                    requestModels.add(
                            new WaTagNode(mapTablesDOs.get(e.getTableId()), e.getAlarm())
                    );
                }
            });

            waSetTagValue.exchange(requestModels);
        }
    }

    @Override
    public void resetAlarmSign() {
        List<AlarmTestAction> alarmActions = almService.findAll();
        List<AlarmDO> listDO = this.findDOByTables(alarmActions);

        //filter
        Map allActiveTags = waGetTagValue.getMap();
        List<WaTagNode> requestModels = new ArrayList<>();
        listDO.forEach(e -> {
            if (allActiveTags.containsKey(e.getCorrespondDO())) {
                requestModels.add(
                        new WaTagNode(e.getCorrespondDO(), 0)
                );
            }
        });

        //reset
        waSetTagValue.exchange(requestModels);
    }

    private List<AlarmDO> findDOByTables(List<AlarmTestAction> alarmActions) {
        //find table ID
        List<String> tableIds = alarmActions.stream()
                .map(e -> e.getTableId())
                .collect(Collectors.toList());

        //get correspond DO
        return alarmDOService.findDOByTables(tableIds);
    }
}
