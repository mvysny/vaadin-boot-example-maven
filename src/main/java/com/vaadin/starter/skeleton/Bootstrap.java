package com.vaadin.starter.skeleton;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

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
        // possibly stop & kill background task executor
    }
}
