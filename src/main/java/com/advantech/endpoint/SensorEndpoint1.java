/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 105/01/14改為主要show出當下sensor資料的websocket server 改良版
 * 參考來源 : 
 * websocket : https://dzone.com/articles/sample-java-web-socket-client
 * quartz : http://potatolattle.blogspot.tw/2013/10/java-classquartz.html
 */
package com.advantech.endpoint;

import com.advantech.helper.CronTrigMod;
import com.advantech.quartzJob.PollingServer;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Wei.Cheng
 */
@ServerEndpoint("/echo2")
public class SensorEndpoint1 {

    private static final Logger log = LoggerFactory.getLogger(SensorEndpoint1.class);
    private static final Queue<Session> queue = new ConcurrentLinkedQueue<>();

    private final JobKey jobKey = new JobKey("JobB", "JobBGroup");
    private final TriggerKey trigKey = new TriggerKey("JobBTrigger", "JobBGroup");

    @OnOpen
    public void onOpen(final Session session) {
        queue.add(session);
        //每次當client連接進來時，去看目前session的數量 當有1個session時把下方quartz job加入到schedule裏頭(只要執行一次，不要重複加入)
        int a = queue.size();
        if (a == 1) {
            pollingDBAndBrocast();
            System.out.println("Some session exist, begin polling.");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        //無作用，目前暫時當作echo測試
        System.out.println("received msg " + message + " from " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        queue.remove(session);
        //當client端完全沒有連結中的使用者時，把job給關閉(持續執行浪費性能)
        if (queue.isEmpty()) {
            unPollingDB();
            System.out.println("All session closed");
        }
    }

    @OnError
    public void error(Session session, Throwable t) {
        queue.remove(session);
    }

    ///Brocast the servermessage to all online users.
    public static void sendAll(String msg) {
        try {
            /* Send the new rate to all open WebSocket sessions */
            ArrayList<Session> closedSessions = new ArrayList<>();
            for (Session session : queue) {
                if (!session.isOpen()) {
                    closedSessions.add(session);
                } else {
                    session.getBasicRemote().sendText(msg);
                }
            }
            queue.removeAll(closedSessions);
        } catch (Throwable ex) {
            log.error(ex.toString());
        }
    }

    // Generate when connect users are at least one.
    private void pollingDBAndBrocast() {
        String crontrigger = "0/10 * 6-18 ? * MON-FRI *";
        try {
            CronTrigMod.getInstance().generateAJob(PollingServer.class, jobKey, trigKey, crontrigger);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }

    // Delete when all users are disconnect.
    private void unPollingDB() {
        try {
            CronTrigMod.getInstance().removeAJob(jobKey, trigKey);
        } catch (SchedulerException ex) {
            log.error(ex.toString());
        }
    }
}
