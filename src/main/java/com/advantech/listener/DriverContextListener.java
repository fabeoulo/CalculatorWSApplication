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
import javax.servlet.annotation.WebListener;

/**
 * See
 * https://stackoverflow.com/questions/3320400/to-prevent-a-memory-leak-the-jdbc-driver-has-been-forcibly-unregistered/23912257#23912257
 * https://www.panziye.com/java/4844.html
 *
 * @author Justin.Yeh
 */
@WebListener
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
                    event.getServletContext().log("Deregistering JDBC driver " + driver);
                    DriverManager.deregisterDriver(driver);
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
