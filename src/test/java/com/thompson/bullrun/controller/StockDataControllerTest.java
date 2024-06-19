package com.thompson.bullrun.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class StockDataControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StockDataController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRSSFeedReturnsSuccessfully() {
        String expectedResponseBody = "{\"item\":[]}";
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(expectedResponseBody);
        ResponseEntity<String> response = controller.getRSSFeed("TEST");
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void getRSSFeedReturnsNull() {
        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(null);
        ResponseEntity<String> response = controller.getRSSFeed("TEST");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void fetchDataReturnsSuccessfully() {
        String expectedResponseBody = "Data for TEST";
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(new ResponseEntity<>(expectedResponseBody, HttpStatus.OK));
        ResponseEntity<String> response = controller.fetchData("url", "TEST", "apiKey");
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void fetchDataReturnsError() {
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
        ResponseEntity<String> response = controller.fetchData("url", "TEST", "apiKey");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

