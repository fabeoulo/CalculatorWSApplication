/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.dao.db1.ModelSopRemarkDetailDAO;
import com.advantech.model.db1.Bab;
import com.advantech.model.db1.BabSettingHistory;
import com.advantech.model.db1.ModelSopRemark;
import com.advantech.model.db1.ModelSopRemarkDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class ModelSopRemarkDetailService {

    @Autowired
    private ModelSopRemarkDetailDAO modelSopRemarkDetailDAO;

    @Autowired
    private BabSettingHistoryService babSettingHistoryService;

    public List<ModelSopRemarkDetail> findAll() {
        return modelSopRemarkDetailDAO.findAll();
    }

    public ModelSopRemarkDetail findByPrimaryKey(Object obj_id) {
        return modelSopRemarkDetailDAO.findByPrimaryKey(obj_id);
    }

    public List<ModelSopRemarkDetail> findByTagName(String tagName) {
        BabSettingHistory setting = babSettingHistoryService.findFirstProcessingByTagName(tagName);
        if (setting == null) {
            return new ArrayList();
        }
        Bab b = setting.getBab();
        return this.findByModelAndPeopleAndStation(b.getModelName(), b.getPeople(), setting.getStation());
    }

    public List<ModelSopRemarkDetail> findByModelAndPeopleAndStation(String modelName, int people, int station) {
        List<ModelSopRemarkDetail> l = this.findPeopleMatchDetail(modelName, people);
        if (!l.isEmpty()) {
            ModelSopRemark m = l.get(0).getModelSopRemark();
            Hibernate.initialize(m);
            List<ModelSopRemarkDetail> stationData = l.stream()
                    .filter(p -> p.getStation() == station)
                    .collect(toList());
            return stationData;
        }
        return l;
    }

    public List<ModelSopRemarkDetail> findPeopleMatchDetail(String modelName, int people) {
        List<ModelSopRemarkDetail> l = modelSopRemarkDetailDAO.findByModelName(modelName);
        Map<ModelSopRemark, List<ModelSopRemarkDetail>> groupingResult = l.stream().collect(groupingBy(ModelSopRemarkDetail::getModelSopRemark));
        return groupingResult.values().stream().filter(list -> list.size() == people).findFirst().orElse(new ArrayList());
    }

    public int insert(ModelSopRemarkDetail pojo) {
        return modelSopRemarkDetailDAO.insert(pojo);
    }

    public int update(ModelSopRemarkDetail pojo) {
        return modelSopRemarkDetailDAO.update(pojo);
    }

    public int delete(ModelSopRemarkDetail pojo) {
        return modelSopRemarkDetailDAO.delete(pojo);
    }

}
