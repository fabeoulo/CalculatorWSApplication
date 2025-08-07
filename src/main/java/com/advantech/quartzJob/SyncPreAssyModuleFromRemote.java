/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.quartzJob;

import static com.advantech.helper.StringParser.defaultStringIfNull;
import com.advantech.model.db1.Floor;
import com.advantech.model.db1.LineType;
import com.advantech.model.db1.PreAssyModuleStandardTime;
import com.advantech.model.db1.PreAssyModuleType;
import com.advantech.service.db1.FloorService;
import com.advantech.service.db1.LineTypeService;
import com.advantech.service.db1.PreAssyModuleStandardTimeService;
import com.advantech.service.db1.PreAssyModuleTypeService;
import com.advantech.service.db3.SqlViewService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class SyncPreAssyModuleFromRemote {

    private static final Logger logger = LoggerFactory.getLogger(SyncPreAssyModuleFromRemote.class);

    @Autowired
    private PreAssyModuleStandardTimeService preAssyModuleStandardTimeService;

    @Autowired
    private PreAssyModuleTypeService preAssyModuleTypeService;

    @Autowired
    @Qualifier("sqlViewService3")
    private SqlViewService sqlViewService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private LineTypeService lineTypeService;

    private Map<String, PreAssyModuleType> moduleTypeMap;
    private Map<String, PreAssyModuleStandardTime> preAssyModuleStMap;
    private LineType lt;
    private Floor floor;
    private final int _floorId = 7;

    public void execute() {
        List<Map> remoteData = sqlViewService.findPreAssyModule();
        logger.info("RemoteData size: " + remoteData.size());
        if (remoteData.isEmpty()) {
            return;
        }

        lt = lineTypeService.findByPrimaryKey(1);
        floor = floorService.findByPrimaryKey(_floorId);
        setModuleTypeMap();
        setPreAssyModuleStMap();
        logger.info("PreAssyModuleStandardTime size before: " + preAssyModuleStMap.size());

        for (Map m : remoteData) {
            String moduleName = defaultStringIfNull(m.get("moduleName"), "");
            String moduleNo = defaultStringIfNull(m.get("moduleNo"), "");
            String modelName = defaultStringIfNull(m.get("modelName"), "");
            BigDecimal ct = new BigDecimal(defaultStringIfNull(m.get("ct"), "0"));

            checkModuleType(moduleName, moduleNo);
            checkPreAssyModuleSt(moduleNo, modelName, ct);
        }
        logger.info("PreAssyModuleStandardTime size after: " + preAssyModuleStMap.size());
    }

    private void setModuleTypeMap() {
        List<PreAssyModuleType> moduleTypes = preAssyModuleTypeService.findAll();
        moduleTypeMap = moduleTypes.stream()
                .filter(m -> m.getLineType().getId() == lt.getId() && m.getModuleNo() != null)
                .collect(Collectors.toMap(PreAssyModuleType::getModuleNo, pmt -> pmt));
    }

    private void checkModuleType(String moduleName, String moduleNo) {
        String preassyModuleName = "(前置)" + moduleName;
        if (moduleTypeMap.containsKey(moduleNo)) {
            PreAssyModuleType pojo = moduleTypeMap.get(moduleNo);
            if (!pojo.getName().equals(preassyModuleName)) {
                pojo.setName(preassyModuleName);
                preAssyModuleTypeService.update(pojo);
            }
        } else {
            PreAssyModuleType pojo = new PreAssyModuleType(preassyModuleName, lt, moduleNo);
            preAssyModuleTypeService.insert(pojo);

            moduleTypeMap.put(moduleNo, pojo);
        }
    }

    private void setPreAssyModuleStMap() {
        List<PreAssyModuleStandardTime> preAssyModuleSts = preAssyModuleStandardTimeService.findAll();
        preAssyModuleStMap = preAssyModuleSts.stream()
                .collect(Collectors.toMap(pmst -> pmst.getModelName() + "~" + pmst.getPreAssyModuleType().getId(), pmst -> pmst));
    }

    private void checkPreAssyModuleSt(String moduleNo, String modelName, BigDecimal ct) {
        PreAssyModuleType mt = moduleTypeMap.get(moduleNo);
        String preAssyModuleKey = modelName + "~" + mt.getId();
        if (preAssyModuleStMap.containsKey(preAssyModuleKey)) {
            PreAssyModuleStandardTime pojo = preAssyModuleStMap.get(preAssyModuleKey);
            if (pojo.getStandardTimeRemote().compareTo(ct) != 0) {
                pojo.setStandardTimeRemote(ct);
                preAssyModuleStandardTimeService.update(pojo);
            }
        } else {
            PreAssyModuleStandardTime pojo = new PreAssyModuleStandardTime();
            pojo.setModelName(modelName);
            pojo.setPreAssyModuleType(mt);
            pojo.setStandardTimeRemote(ct);
            pojo.setStandardTime(BigDecimal.ZERO);
            pojo.setFloor(floor);
            preAssyModuleStandardTimeService.insert(pojo);

            preAssyModuleStMap.put(preAssyModuleKey, pojo);
        }
    }

}
