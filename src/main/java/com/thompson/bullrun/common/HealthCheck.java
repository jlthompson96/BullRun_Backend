package com.thompson.bullrun.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
public class HealthCheck {
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
