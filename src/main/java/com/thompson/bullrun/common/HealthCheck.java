package com.thompson.bullrun.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is a REST controller that provides a health check endpoint for the service.
 * It uses the Lombok library for logging and Spring's RestController and GetMapping annotations to define the endpoint.
 */
@Slf4j
@RestController
public class HealthCheck {

    /**
     * This method is mapped to the "/health" endpoint and returns a ResponseEntity with a message indicating the service is running.
     * It also logs the current date and time, and the fact that the health check endpoint was called.
     *
     * @return ResponseEntity with a message indicating the service is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        log.info("Calling health check endpoint");
        log.info("Service is up and running as of {}", formattedDateTime);
        return ResponseEntity.ok("Service is up and running as of " + formattedDateTime);
    }
}
