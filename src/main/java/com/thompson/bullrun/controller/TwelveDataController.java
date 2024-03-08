package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/stockData")
public class TwelveDataController {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String stockPriceURL;
    private final String companyProfileURL;
    private final String companyLogoURL;
    private final String previousCloseURL;

    @Autowired
    public TwelveDataController(RestTemplate restTemplate,
                                @Value("${twelveDataAPIKey}") String apiKey,
                                @Value("${stockPrice}") String stockPriceURL,
                                @Value("${companyProfile}") String companyProfileURL,
                                @Value("${companyLogo}") String companyLogoURL,
                                @Value("${previousClose}") String previousCloseURL){
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.stockPriceURL = stockPriceURL;
        this.companyProfileURL = companyProfileURL;
        this.companyLogoURL = companyLogoURL;
        this.previousCloseURL = previousCloseURL;
    }

    @GetMapping("/companyProfile")
    public ResponseEntity<String> getCompanyProfile(@RequestParam String symbol) {
        log.info("Getting company profile for symbol: {}", symbol);
        return fetchData(companyProfileURL, symbol);
    }

    @GetMapping("/companyLogo")
    public ResponseEntity<String> getCompanyLogo(@RequestParam String symbol) {
        log.info("Getting company logo for symbol: {}", symbol);
        return fetchData(companyLogoURL, symbol);
    }

    @GetMapping("/stockPrice")
    public ResponseEntity<String> getStockPrice(@RequestParam String symbol) {
        log.info("Getting stock price for symbol: {}", symbol);
        return fetchData(stockPriceURL, symbol);
    }

    @GetMapping("/previousClose")
    public ResponseEntity<String> getPreviousClose(@RequestParam String symbol) {
        log.info("Getting previous close for symbol: {}", symbol);
        return fetchData(previousCloseURL, symbol);
    }

    private ResponseEntity<String> fetchData(String url, String symbol) {
        try {
            String apiUrl = url.replace("{apiKey}", apiKey).replace("{symbol}", symbol);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Response: {}", responseEntity.getBody());
                return responseEntity;
            } else {
                log.error("Error in fetchData method");
                log.error(responseEntity.getStatusCode().toString());
                return ResponseEntity.status(responseEntity.getStatusCode()).body("Error retrieving data");
            }
        } catch (Exception e) {
            log.error("Error in fetchData method");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving data");
        }
    }
}
