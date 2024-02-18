package com.thompson.bullrun.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TwelveDataControllerTest {

    private TwelveDataController twelveDataController;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        twelveDataController = new TwelveDataController(restTemplate, "testApiKey", "testStockPriceURL");
    }

    @Test
    public void testRetrieveDataFromApi_Success() {
        String symbol = "AAPL";
        String mockResponse = "{\"symbol\":\"AAPL\",\"price\":150.23}";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().contains("AAPL"));
        assertTrue(responseEntity.getBody().contains("150.23"));
    }

    @Test
    public void testRetrieveDataFromApi_InvalidSymbol() {
        String symbol = "INVALID";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testRetrieveDataFromApi_InternalServerError() {
        String symbol = "AAPL";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }

    @Test
    public void testRetrieveDataFromApi_NullResponse() {
        String symbol = "AAPL";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(null);

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertNull(responseEntity);
    }

    @Test
    public void testRetrieveDataFromApi_EmptyResponse() {
        String symbol = "AAPL";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isEmpty());
    }

    @Test
    public void testRetrieveDataFromApi_MalformedResponse() {
        String symbol = "AAPL";
        String mockResponse = "malformed response";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().contains("malformed response"));
    }

    @Test
    public void testRetrieveDataFromApi_MultipleSymbols() {
        String symbol = "AAPL,GOOGL";
        String mockResponse = "{\"symbol\":\"AAPL\",\"price\":150.23}\n{\"symbol\":\"GOOGL\",\"price\":2500.00}";

        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<String> responseEntity = twelveDataController.retrieveDataFromApi(symbol);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().contains("AAPL"));
        assertTrue(responseEntity.getBody().contains("150.23"));
        assertTrue(responseEntity.getBody().contains("GOOGL"));
        assertTrue(responseEntity.getBody().contains("2500.00"));
    }

    // Write more test cases for other scenarios as described in the previous response...
}

