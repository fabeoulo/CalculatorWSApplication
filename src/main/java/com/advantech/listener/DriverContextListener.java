/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.listener;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author Justin.Yeh
 */
public class DriverContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        // nothing to do
    }

    // Now deregister JDBC drivers in this context's ClassLoader:
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Get the webapp's ClassLoader
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    DriverManager.deregisterDriver(driver);
                    event.getServletContext().log("Deregistering JDBC driver " + driver);
                } catch (SQLException ex) {
                    event.getServletContext().log("Driver deregistration failure.", ex);
                }
            } else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                event.getServletContext().log("Not deregistering JDBC driver " + driver + " as it does not belong to this webapp's ClassLoader");
            }
        }
    }
}
