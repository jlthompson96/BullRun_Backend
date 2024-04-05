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

@ExtendWith(MockitoExtension.class)
class TwelveDataControllerTest {

    @Mock
    private RestTemplate restTemplate;

    private final String testSymbol = "TEST";

    @Test
    void testGetCompanyProfile() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "", "companyProfileURL", "", "");
        String expectedResponseBody = "Company profile for TEST";
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(new ResponseEntity<>(expectedResponseBody, HttpStatus.OK));
        ResponseEntity<String> response = controller.getCompanyProfile(testSymbol);
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testGetCompanyLogo() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "", "", "companyLogoURL", "");
        String expectedResponseBody = "Company logo for TEST";
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(new ResponseEntity<>(expectedResponseBody, HttpStatus.OK));
        ResponseEntity<String> response = controller.getCompanyLogo(testSymbol);
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testGetStockPrice() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "stockPriceURL", "", "", "");
        String expectedResponseBody = "Stock price for TEST";
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(new ResponseEntity<>(expectedResponseBody, HttpStatus.OK));
        ResponseEntity<String> response = controller.getStockPrice(testSymbol);
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testGetPreviousClose() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "", "", "", "previousCloseURL");
        String expectedResponseBody = "Previous close for TEST";
        when(restTemplate.getForEntity(any(String.class), eq(String.class))).thenReturn(new ResponseEntity<>(expectedResponseBody, HttpStatus.OK));
        ResponseEntity<String> response = controller.getPreviousClose(testSymbol);
        assertEquals(expectedResponseBody, response.getBody());
    }

    @Test
    void testGetIndexPrices() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "stockPriceURL", "", "", "");
        Map<String, String> expectedResponseMap = new HashMap<>();
        expectedResponseMap.put("DJI", "1,000.00");
        expectedResponseMap.put("SP500", "2,000.00");
        expectedResponseMap.put("NASDAQ", "3,000.00");

        JSONObject djiJsonObject = new JSONObject().put("price", "1000.00");
        JSONObject spxJsonObject = new JSONObject().put("price", "2000.00");
        JSONObject ndxJsonObject = new JSONObject().put("price", "3000.00");

        when(restTemplate.getForEntity(any(String.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>(djiJsonObject.toString(), HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(spxJsonObject.toString(), HttpStatus.OK))
                .thenReturn(new ResponseEntity<>(ndxJsonObject.toString(), HttpStatus.OK));

        Map<String, String> response = controller.getIndexPrices();
        assertEquals(expectedResponseMap, response);
    }

    @Test
    void testFormatPrice() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "", "", "", "");
        double price = 1234.5678;
        String formattedPrice = controller.formatPrice(price);
        assertEquals("1,234.57", formattedPrice);
    }

    @Test
    void testHealthCheck() {
        TwelveDataController controller = new TwelveDataController(restTemplate, "", "", "", "", "");
        ResponseEntity<String> response = controller.healthCheck();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}

