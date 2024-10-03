package com.vaadin.starter.skeleton;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * The {@link #contextInitialized(ServletContextEvent)} is called once,
 * before the Vaadin app receives any requests. The {@link #contextDestroyed(ServletContextEvent)}
 * is called exactly once when the app is shutting down.
 */
@WebListener
public class Bootstrap implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // to be implemented by actual apps.
        // possibly setup database connections, or initialize background task executor
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // to be implemented by actual apps.
        // possibly stop & kill background task executor, or cleanup temp files, or such.
    }
}
