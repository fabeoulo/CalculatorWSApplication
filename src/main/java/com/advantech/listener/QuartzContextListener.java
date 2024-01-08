/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.listener;

import com.advantech.endpoint.Endpoint;
import com.advantech.endpoint.Endpoint2;
import com.advantech.endpoint.Endpoint4;
import com.advantech.helper.ThreadLocalCleanUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
public class QuartzContextListener implements ServletContextListener {

    //this listener is unused http://stackoverflow.com/questions/19573457/simple-example-for-quartz-2-2-and-tomcat-7
    //quartz only need to modify at web.xml & the quartz properties/xml to start, stop, wait
    
    private static final Logger log = LoggerFactory.getLogger(QuartzContextListener.class);

    public QuartzContextListener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        try {
//            CronTrigMod.getInstance().unScheduleAllJob();
            
//            Endpoint.clearSessions();
//            Endpoint2.clearSessions();
//            Endpoint4.clearSessions();

            Thread.sleep(3000);
            ThreadLocalCleanUtil.clearThreadLocals();
        } catch (Exception e) {
            sce.getServletContext().log(e.toString());
        }

        //web service當tomcat在做reload會有資源為釋放之情形(多次reload可能會memory leak)
        //http://timen-zbt.iteye.com/blog/1814795
        //安裝quartz後當tomcat重新啟動時會出現memory leak(http://www.cnblogs.com/leeying/p/3782102.html)
        //使用以上方法
    }
}
