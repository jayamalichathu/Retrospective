package com.test.retrospective;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class to start the Retrospective application.
 */
@SpringBootApplication
public class RetrospectiveApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrospectiveApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(RetrospectiveApplication.class, args);
        LOGGER.info("Application started");
    }
}
