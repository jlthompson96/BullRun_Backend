package com.thompson.bullrun.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TwelveDataControllerTest {

    @Mock
    private RestTemplate restTemplate;

    private TwelveDataController twelveDataController;

    private final String apiKey = "your-api-key";
    private final String stockPriceURL = "https://api.example.com/stock/{symbol}/price?apikey={apiKey}";
    private final String companyProfileURL = "https://api.example.com/company/{symbol}/profile?apikey={apiKey}";
    private final String companyLogoURL = "https://api.example.com/company/{symbol}/logo?apikey={apiKey}";
    private final String previousCloseURL = "https://api.example.com/stock/{symbol}/previousclose?apikey={apiKey}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        twelveDataController = new TwelveDataController(restTemplate, apiKey, stockPriceURL,
                companyProfileURL, companyLogoURL, previousCloseURL);
    }

    @Test
    void testGetCompanyProfile_Success() {
        String symbol = "AAPL";
        String expectedResponse = "Company profile data";

        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(new ResponseEntity<>(expectedResponse, HttpStatus.OK));

        ResponseEntity<String> response = twelveDataController.getCompanyProfile(symbol);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    void testGetCompanyLogo_NotFound() {
        String symbol = "GOOG";

        when(restTemplate.getForEntity(any(String.class), any())).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = twelveDataController.getCompanyLogo(symbol);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // Add similar test cases for other controller methods
}
