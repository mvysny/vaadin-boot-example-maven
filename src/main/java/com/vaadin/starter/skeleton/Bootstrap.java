package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.UI;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * The {@link #contextInitialized(ServletContextEvent)} is called once,
 * before the Vaadin app receives any requests. The {@link #contextDestroyed(ServletContextEvent)}
 * is called exactly once when the app is shutting down.
 */
@WebListener
public class Bootstrap implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    /**
     * Runs jobs in background thread.
     */
    private static volatile ExecutorService executorService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // to be implemented by actual apps.
        // possibly setup database connections, or initialize background task executor
        executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(10L, TimeUnit.SECONDS)) {
                // stop being nice: shutdown jobs immediately, interrupting all ongoing jobs.
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Error while shutting down background jobs", e);
        }
    }

    private static final class UIExecutor implements Executor {
        @NotNull
        private final UI ui;

        public UIExecutor() {
            this.ui = Objects.requireNonNull(UI.getCurrent(), "must be called from Vaadin ui thread");
        }

        @Override
        public void execute(@NotNull Runnable command) {
            Objects.requireNonNull(command);
            ui.access(command::run);
        }
    }

    public interface Job {
        /**
         * Runs the job synchronously.
         * @param uiExecutor used to update Vaadin UI.
         * @throws Exception if the job fails.
         */
        void run(@NotNull Executor uiExecutor) throws Exception;
    }

    @NotNull
    public static Future<?> submitBackgroundJob(@NotNull Job job) {
        final Executor uiExecutor = new UIExecutor();
        return executorService.submit(() -> {
            try {
                job.run(uiExecutor);
            } catch (Throwable t) {
                log.error("Error while executing background job", t);
            }
        });
    }
}
