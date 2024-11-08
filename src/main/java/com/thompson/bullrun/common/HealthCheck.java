package com.thompson.bullrun.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * REST controller that provides a health check endpoint for the service.
 * Uses Lombok for logging and Spring's RestController and GetMapping annotations to define the endpoint.
 */
@Slf4j
@RestController
public class HealthCheck {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Handles the "/health" endpoint to verify service health status.
     * Logs the current date and time and indicates that the health check endpoint was called.
     *
     * @return ResponseEntity with a message indicating the service is running
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        String formattedDateTime = currentDateTime.format(DATE_TIME_FORMATTER);

        log.info("Health check endpoint called at {}", formattedDateTime);

        String responseMessage = "Service is up and running as of " + formattedDateTime;
        log.info("Health check status: {}", responseMessage);

        return ResponseEntity.ok(responseMessage);
    }
}
