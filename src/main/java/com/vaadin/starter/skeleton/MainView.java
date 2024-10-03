package com.vaadin.starter.skeleton;

import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * The main view contains a button and a click listener.
 */
@Route("")
public class MainView extends VerticalLayout {

    private final Span statusSpan = new Span();
    /**
     * These jobs are meant to be short-running and confined to this view. When the user reloads the page or exits
     * the view, the jobs are canceled.
     * <br/>
     * Long-running jobs (which are possibly tied to the user who committed them)
     * would not update the UI - instead the UI would probably actively poll the jobs
     * for status. Such jobs are not implemented in this example app though.
     */
    private final List<Future<?>> backgroundJobs = new ArrayList<>();

    public MainView() {
        statusSpan.setId("status");

        // Use TextField for standard text input
        TextField textField = new TextField("Your name");
        textField.addClassName("bordered");

        // Button click listeners can be defined as lambda expressions
        Button button = new Button("Run background thread",
                e -> startBackgroundJob(textField.getValue()));

        // Theme variants give you predefined extra styles for components.
        // Example: Primary button is more prominent look.
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // You can specify keyboard shortcuts for buttons.
        // Example: Pressing enter in this view clicks the Button.
        button.addClickShortcut(Key.ENTER);

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField, button, statusSpan);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        backgroundJobs.forEach(it -> it.cancel(true));
        backgroundJobs.clear();
        super.onDetach(detachEvent);
    }

    private void startBackgroundJob(@NotNull final String name) {
        statusSpan.setText("");
        final Future<?> jobFuture = Bootstrap.submitBackgroundJob(uiExecutor -> {
            uiExecutor.execute(() -> statusSpan.setText("Started job " + name + "! Thinking"));
            Thread.sleep(1000L);
            uiExecutor.execute(() -> statusSpan.setText("Thinking some more..."));
            Thread.sleep(1000L);
            uiExecutor.execute(() -> statusSpan.setText("Done!"));
        });
        backgroundJobs.add(jobFuture);
    }
}
