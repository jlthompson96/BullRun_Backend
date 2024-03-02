package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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

    @Value("${stockPrice}")
    private String stockPriceURL;

    @Value("${companyProfile}")
    private String companyProfileURL;

    @Value("${companyLogo}")
    private String companyLogoURL;

    @Value("${previousClose}")
    private String previousCloseURL;


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
            String profileUrl = generateUrl("companyProfile", symbol);
            log.info("Profile URL: {}", profileUrl);

            // Make the GET requests and handle the responses using ResponseEntity
            ResponseEntity<String> profileResponse = restTemplate.getForEntity(profileUrl, String.class);
            log.info("Profile Response: {}", profileResponse.getBody());

            // Create and return a ResponseEntity containing both profile and logo responses
            return profileResponse;
        } catch (Exception e) {
            log.error("Error in getCompanyProfile method");
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @RequestMapping("/getCompanyLogo")
    public ResponseEntity<String> getCompanyLogo(String symbol) {
        log.info("----- Entering getCompanyLogo method ----");
        log.info("Getting company logo for symbol: {}", symbol);
        try {
            // Set the API key in the URL
            String url = generateUrl("companyLogo", symbol);
            log.info("URL: {}", url);

            // Make the GET request and handle the response using ResponseEntity
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            log.info("Response: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception e) {
            log.error("Error in getCompanyLogo method");
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving company logo");
        }
    }

    @RequestMapping("/getStockPrice")
    public ResponseEntity<String> getStockPrice(String symbol) {
        log.info("----- Entering getStockPrice method ----");
        log.info("Getting stock price for symbol: {}", symbol);
        try {
            // Set the API key in the URL
            String url = generateUrl("stockPrice", symbol);
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

    @RequestMapping("/getPreviousClose")
    public ResponseEntity<String> getPreviousClose(String symbol) {
        log.info("----- Entering getPreviousClose method ----");
        log.info("Getting previous close for symbol: {}", symbol);
        try {
            // Set the API key in the URL
            String url = generateUrl("previousClose", symbol);
            log.info("URL: {}", url);

            // Make the GET request and handle the response using ResponseEntity
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            log.info("Response: {}", responseEntity.getBody());
            return responseEntity;
        } catch (Exception e) {
            log.error("Error in getPreviousClose method");
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("Error retrieving previous close");
        }
    }

    private String generateUrl(String type, String symbol) {
        String baseURL = switch (type) {
            case "companyProfile" -> companyProfileURL;
            case "stockPrice" -> stockPriceURL;
            case "companyLogo" -> companyLogoURL;
            case "previousClose" -> previousCloseURL;
            default -> throw new IllegalArgumentException("Invalid type parameter: " + type);
        };
        String url = baseURL.replace("{apiKey}", apiKey)
                .replace("{symbol}", symbol);
        if (type.equals("companyProfile")) {
            url = baseURL.replace("{apiKey}", apiKey)
                    .replace("{symbol}", symbol);
        } else if (type.equals("companyLogo")) {
            url = baseURL.replace("{apiKey}", apiKey)
                    .replace("{symbol}", symbol);
        } else if (type.equals("previousClose")) {
            url = baseURL.replace("{apiKey}", apiKey)
                    .replace("{symbol}", symbol);
        }
        return url;
    }
}
