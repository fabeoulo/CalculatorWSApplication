/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Cell 桌的狀態
 */
package com.advantech.endpoint;

import com.advantech.helper.ApplicationContextHelper;
import com.advantech.helper.CronTrigMod;
import com.advantech.helper.PropertiesReader;
import com.advantech.quartzJob.PollingCellResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint("/echo4")
public class Endpoint4 {

    private static final Logger log = LoggerFactory.getLogger(Endpoint4.class);

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    private static final String POLLING_FREQUENCY;

    private static final String JOB_NAME = "JOB4";

    private final CronTrigMod ctm = (CronTrigMod) ApplicationContextHelper.getBean("cronTrigMod");

    static {
        POLLING_FREQUENCY = ((PropertiesReader) ApplicationContextHelper.getBean("propertiesReader")).getEndpointPollingCron();
    }

    @OnOpen
    public void onOpen(final Session session) {
        //Push the current status on client first connect
        try {
//            Object obj = new PollingCellResult().getData();
//            session.getBasicRemote().sendText(obj.toString());
//            showUrlParam(session);
            throw new IOException("Not finish yet");
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }

        sessions.add(session);
        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        int a = sessions.size();
        if (a == 1) {
            System.out.println("Some session exist, begin polling.");
//            pollingDBAndBrocast();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //無作用，目前暫時當作echo測試
        System.out.println("received msg " + message + " from " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
//        showUrlParam(session);
        sessions.remove(session);
        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
        if (sessions.isEmpty()) {
//            unPollingDB();
            System.out.println("All session closed");
        }
    }

    @OnError
    public void error(Session session, Throwable t) {
        sessions.remove(session);
        log.error(t.getMessage(), t);
    }

    ///Brocast the servermessage to all online users.
    public static void sendAll(String msg) {
        try {
            /* Send the new rate to all open WebSocket sessions */
            ArrayList<Session> closedSessions = new ArrayList<>();
            for (Session session : sessions) {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    session.getBasicRemote().sendText(msg);
                }
            }
            sessions.removeAll(closedSessions);
        } catch (Throwable ex) {
            log.error(ex.toString());
        }
    }

    // Generate when connect users are at least one.
    private void pollingDBAndBrocast() {
        try {
            ctm.scheduleJob(PollingCellResult.class, JOB_NAME, POLLING_FREQUENCY);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    // Delete when all users are disconnect.
    private void unPollingDB() {
        try {
            ctm.removeJob(JOB_NAME);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    public static void clearSessions() {
        sessions.clear();
    }

    private void showUrlParam(Session session) {
        Map map = session.getRequestParameterMap();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            System.out.println("Receive endpoint param: " + map.get(it.next()));
        }
    }

}
