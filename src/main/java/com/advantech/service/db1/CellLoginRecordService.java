/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service.db1;

import com.advantech.model.db1.CellLoginRecord;
import com.advantech.dao.db1.CellLoginRecordDAO;
import com.advantech.model.db1.CellStation;
import com.advantech.model.db1.User;
import static com.google.common.base.Preconditions.*;
import java.util.List;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Jusitn.Yeh
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CellLoginRecordService {

    @Autowired
    private CellLoginRecordDAO cellLoginRecordDAO;

    @Autowired
    private CellStationService cellStationService;

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(CellLoginRecordService.class);

    public List<CellLoginRecord> findAll() {
        return cellLoginRecordDAO.findAll();
    }

    public CellLoginRecord findByPrimaryKey(Object obj_id) {
        return cellLoginRecordDAO.findByPrimaryKey(obj_id);
    }

    public int insert(CellLoginRecord pojo) {
        return cellLoginRecordDAO.insert(pojo);
    }

    public int insert(int cellStationId, String jobnumber) {
        CellStation cellStation = cellStationService.findByPrimaryKey(cellStationId);
        checkDeskIsAvailable(cellStation);
        checkUserIsAvailable(jobnumber);

        User user = userService.findByJobnumber(jobnumber);
        String userName = user == null ? jobnumber : user.getUsernameCh();

        CellLoginRecord pojo = new CellLoginRecord(cellStation, jobnumber);
        pojo.setLastUpdateTime(new DateTime().toDate());
        pojo.setUserName(userName);
        this.insert(pojo);

        return 1;
    }

    public void checkDeskIsAvailable(CellStation cs) {
        checkArgument(cs.getCellLoginRecords() == null || cs.getCellLoginRecords().isEmpty(), "此桌次已有使用者");
    }

    public void checkUserIsAvailable(String jobNumber) {
        CellLoginRecord pojo = cellLoginRecordDAO.findByJobnumber(jobNumber);
        checkArgument(pojo == null, "使用者已在其它桌次使用中");
    }

    public int update(CellLoginRecord pojo) {
        return cellLoginRecordDAO.update(pojo);
    }

    public int changeDeck(String jobnumber) {
        CellLoginRecord pojo = cellLoginRecordDAO.findByJobnumber(jobnumber);
        this.delete(pojo);
        return 1;
    }

    public int delete(CellLoginRecord pojo) {
        return cellLoginRecordDAO.delete(pojo);
    }

    public int delete(String jobnumber) {
        CellLoginRecord pojo = cellLoginRecordDAO.findByJobnumber(jobnumber);
        this.delete(pojo);

        return 1;
    }

    public int cleanTests() {
        return cellLoginRecordDAO.cleanTests();
    }
}
