/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.quartzJob;

import com.advantech.helper.DatetimeGenerator;
import com.advantech.service.BasicService;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
//Clean the data in FBN table before target date.
public class CleanSensorData implements Job {

    private static final Logger log = LoggerFactory.getLogger(CleanSensorData.class);
    private final DatetimeGenerator dg = new DatetimeGenerator("yyyy-MM-dd");
    private static final int SPECIFY_DAY = 7;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        DateTime d = new DateTime();
        d = d.minusDays(SPECIFY_DAY);
        String date = dg.dateFormatToString(d);
        BasicService.getFbnService().sensorDataClean(date);
    }
}