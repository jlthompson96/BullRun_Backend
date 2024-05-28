package com.thompson.bullrun.common;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HealthCheckTest {

    @Test
    void testHealthCheck() {
        HealthCheck controller = new HealthCheck();
        ResponseEntity<String> response = controller.healthCheck();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
