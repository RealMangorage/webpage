package org.mangorage.web;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggerConfiguration;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.metrics.ApplicationStartup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        setLoggingVerbosity();
        var a = (AnnotationConfigApplicationContext) SpringApplication.run(Core.class, args);

        a.addApplicationListener(new ApplicationListener<ApplicationEvent>() {
            @Override
            public void onApplicationEvent(ApplicationEvent event) {
                System.out.println(event);
            }
        });

        System.out.println(a.getClass());
        a.setApplicationStartup(ApplicationStartup.DEFAULT);
        a.getApplicationStartup().start("LOl");
        a.start();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            // Handle interruption if necessary
            Thread.currentThread().interrupt();
        }
    }

    private static void setLoggingVerbosity() {
        // Set logging level for specific packages and classes
        // Example: Set logging level to DEBUG for all packages
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("ROOT").setLevel(Level.DEBUG);
        context.getLogger("org.springframework").setLevel(Level.DEBUG);

        // Example: Set logging level to DEBUG for a specific class
        // LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("org.mangorage.web.Core").setLevel(Level.DEBUG);

        // Alternatively, you can use the logger directly
        //logger.debug("Setting logging verbosity to DEBUG");
    }
}
