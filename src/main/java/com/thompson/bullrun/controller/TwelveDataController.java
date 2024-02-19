package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @Value("${stockPrice}")
    private String stockPriceURL;

    @Value("${companyProfile}")
    private String companyProfileURL;

    @Value("${companyLogo}")
    private String companyLogoURL;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public TwelveDataController(RestTemplate restTemplate,
                                @Value("${twelveDataAPIKey}") String apiKey,
                                @Value("${stockPrice}") String stockPriceURL,
                                @Value("${companyProfile}") String companyProfileURL,
                                @Value("${companyLogo}") String companyLogoURL) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.stockPriceURL = stockPriceURL;
        this.companyProfileURL = companyProfileURL;
        this.companyLogoURL = companyLogoURL;
    }

    @RequestMapping("/getCompanyProfile")
    public ResponseEntity<String> getCompanyProfile(String symbol) {
        log.info("----- Entering getCompanyProfile method ----");
        log.info("Getting company profile for symbol: {}", symbol);
        try {
            // Set the API key in the URL
            String url = companyProfileURL.replace("{apiKey}", apiKey);
            String companyLogo = companyLogoURL.replace("{apiKey}", apiKey);
            url = url.replace("{symbol}", symbol);
            companyLogo = companyLogo.replace("{symbol}", symbol);
            log.info("URL: {}", url);

            // Make the GET request and handle the response using ResponseEntity
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url+companyLogo, String.class);
            log.info("Response: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception e) {
            log.error("Error in getCompanyProfile method");
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving company profile");
        }
    }

    @RequestMapping("/getStockPrice")
    public ResponseEntity<String> getStockPrice(String symbol) {
        log.info("----- Entering getStockPrice method ----");
        log.info("Getting stock price for symbol: {}", symbol);
        try {
            // Set the API key in the URL
            String url = stockPriceURL.replace("{apiKey}", apiKey);
            url = url.replace("{symbol}", symbol);
            log.info("URL: {}", url);

            // Make the GET request and handle the response using ResponseEntity
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            log.info("Response: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception e) {
            log.error("Error in getStockPrice method");
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving stock price");
        }
    }
}
