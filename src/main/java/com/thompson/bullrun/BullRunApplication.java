package com.thompson.bullrun;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.InetAddress;

/**
 * The BullRunApplication class is the entry point of the Spring Boot application.
 * It uses the Lombok library for logging.
 */
@Slf4j
@OpenAPIDefinition
@SpringBootApplication
public class BullRunApplication {

    /**
     * The main method is the entry point of the Java application.
     * It starts the Spring Boot application and logs the start-up status.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Start the Spring Boot application
            SpringApplication.run(BullRunApplication.class, args);

            // Retrieve and log the IP address
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            log.info("---------------------------------------");
            log.info("BullRunApplication started successfully");
            log.info("Running on IP address: {}", ipAddress);
            log.info("---------------------------------------");
        } catch (Exception e) {
            // Log the error message if the application fails to start
            log.error("Error starting BullRunApplication");
            log.error(e.getMessage());
        }
    }
}