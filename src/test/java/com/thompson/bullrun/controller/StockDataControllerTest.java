package com.thompson.bullrun.controller;

import com.thompson.bullrun.services.DailyStockDataService;
import com.thompson.bullrun.services.StockService;
import com.thompson.bullrun.entities.StockEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StockDataControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DailyStockDataService dailyStockDataService;

    @Mock
    private StockService stockService;

    @Value("${twelveDataAPIKey}")
    private String twelveDataAPIKey;

    @Value("${polygonAPIKey}")
    private String polygonAPIKey;

    @Value("${stockPrice}")
    private String stockPriceURL;

    @Value("${companyProfile}")
    private String companyProfileURL;

    @Value("${previousClose}")
    private String previousCloseURL;

    @Value("${companyLogo}")
    private String companyLogoURL;

    @InjectMocks
    private StockDataController stockDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addStock_addsStockSuccessfully() {
        StockEntity stockEntity = new StockEntity();
        stockEntity.setSymbol("AAPL");

        ResponseEntity<StockEntity> response = stockDataController.addStock(stockEntity);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stockEntity, response.getBody());
    }

    @Test
    void deleteStock_deletesStockSuccessfully() {
        Map<String, Object> request = Map.of("stockTicker", "AAPL");

        ResponseEntity<String> response = stockDataController.deleteStock(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully deleted stock", response.getBody());
    }

    @Test
    void updateSharesOwned_updatesSharesSuccessfully() {
        Map<String, Object> request = Map.of("symbol", "AAPL", "sharesOwned", 100);

        ResponseEntity<String> response = stockDataController.updateSharesOwned(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Shares owned updated successfully", response.getBody());
    }
}