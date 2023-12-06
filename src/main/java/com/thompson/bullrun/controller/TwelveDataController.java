package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/stockData")
public class TwelveDataController {

    @Value("${twelveDataAPIKey}")
    private String apiKey;

    private String stockTicker = "TSLA";
    //Market Movers
    private final String marketMoversList = "https://api.twelvedata.com/market_movers/price?apikey={apiKey}";

    //Stock Price
    private final String currentStockPrice = "https://api.twelvedata.com/price?symbol="+ stockTicker +"&&apikey={apiKey}";


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/getMovers")
    public ResponseEntity<String> getMarketMoversList() {
        log.info("----- Entering getMarketMoversList method ----");

        // Set the API key in the URL
        String url = marketMoversList.replace("{apiKey}", apiKey);

        // Make the GET request and handle the response using ResponseEntity
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        // You can access various details from the ResponseEntity if needed
        // For example, HttpStatus, headers, etc.
        // HttpStatus statusCode = responseEntity.getStatusCode();
        // HttpHeaders headers = responseEntity.getHeaders();

        return responseEntity;
    }

    @RequestMapping("/getStockPrice")
    public ResponseEntity<String> getCurrentStockPrice() {
    log.info("----- Entering getCurrentStockPrice method ----");

        // Set the API key in the URL
        String url = currentStockPrice.replace("{apiKey}", apiKey);
        log.info("url: {}", url);

        // Make the GET request and handle the response using ResponseEntity
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        log.info("responseEntity: {}", responseEntity);

        return responseEntity;
    }
}
