/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.model.db1.Worktime;
import com.advantech.model.db1.WorktimeM6;
import com.advantech.model.db1.WorktimeExtras;
import com.advantech.service.db1.WorktimeExtrasService;
import com.advantech.service.db1.WorktimeM6Service;
import com.advantech.service.db1.WorktimeService;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
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
public class SyncWorktimeFromRemote {

    private static final Logger logger = LoggerFactory.getLogger(SyncWorktimeFromRemote.class);

    @Autowired
    private WorktimeService worktimeService;

    @Autowired
    private WorktimeM6Service worktimeM6Service;

    @Autowired
    @Qualifier("sqlViewService3")
    private com.advantech.service.db3.SqlViewService sqlViewServiceM3;

    @Autowired
    @Qualifier("sqlViewService7")
    private com.advantech.service.db7.SqlViewService sqlViewServiceM6;

    @Autowired
    private WorktimeExtrasService worktimeExtrasService;

    public void execute() {
        syncWorktime();
        syncWorktimeExtras();
    }

    private void syncWorktime() {
        logger.info("Sync Worktime start ");

        List<Worktime> remoteData = new ArrayList<>();
        remoteData.addAll(sqlViewServiceM3.findWorktime());

        List<Worktime> remoteM6List = sqlViewServiceM6.findWorktime()
                .stream()
                .filter(w -> !remoteData.stream().anyMatch(r -> r.getModelName().equals(w.getModelName()))).collect(toList());
        remoteData.addAll(remoteM6List);

        worktimeService.deleteAll();
        worktimeService.insert(remoteData);
        logger.info("Sync Worktime end " + remoteData.size());

        List<WorktimeM6> remoteM6Model = sqlViewServiceM6.findWorktimeM6();
        worktimeM6Service.deleteAll();
        worktimeM6Service.insert(remoteM6Model);
        logger.info("Sync WorktimeM6 end " + remoteM6Model.size());

    }

    private void syncWorktimeExtras() {
        logger.info("Sync WorktimeExtras start ");
        List<WorktimeExtras> remoteData = new ArrayList<>();
        remoteData.addAll(sqlViewServiceM3.findExtras());

        worktimeExtrasService.deleteAll();
        worktimeExtrasService.insert(remoteData);
        logger.info("Sync WorktimeExtras end " + remoteData.size());
    }

}
