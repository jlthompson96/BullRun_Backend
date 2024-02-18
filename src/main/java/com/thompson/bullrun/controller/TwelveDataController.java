package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/stockData")
public class TwelveDataController {

    @Value("${twelveDataAPIKey}")
    private String apiKey;

    //Stock Price
    @Value("${stockPrice}")
    private String stockPriceURL;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public TwelveDataController(RestTemplate restTemplate,
                                @Value("${twelveDataAPIKey}") String apiKey,
                                @Value("${stockPrice}") String stockPriceURL) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.stockPriceURL = stockPriceURL;
    }

    @RequestMapping("/getStockPrice")
    public ResponseEntity<String> retrieveDataFromApi(String symbol) {
        log.info("----- Entering getStockPrice method ----");
        log.info("Getting stock price for symbol: {}", symbol);

        // Set the API key in the URL
        String url = stockPriceURL.replace("{apiKey}", apiKey);
        url = url.replace("{symbol}", symbol);
        log.info("URL: {}", url);

        // Make the GET request and handle the response using ResponseEntity
        return restTemplate.getForEntity(url, String.class);
    }
}
