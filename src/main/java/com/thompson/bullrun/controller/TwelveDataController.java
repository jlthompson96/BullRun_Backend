package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static java.lang.Math.round;

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
                                @Value("${previousClose}") String previousCloseURL) {
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

    @GetMapping("/indexPrices")
    public Map<String, String> getIndexPrices() {
        log.info("Getting index prices");
        ResponseEntity<String> djiPrice = fetchData(stockPriceURL, "DJI");
        ResponseEntity<String> spxPrice = fetchData(stockPriceURL, "SPX");
        ResponseEntity<String> ndxPrice = fetchData(stockPriceURL, "IXIC");

        JSONObject json = new JSONObject();
        json.put("DJI", new JSONObject(Objects.requireNonNull(djiPrice.getBody())).getString("price"));
        json.put("S&P500", new JSONObject(Objects.requireNonNull(spxPrice.getBody())).getString("price"));
        json.put("NASDAQ", new JSONObject(Objects.requireNonNull(ndxPrice.getBody())).getString("price"));

        // Convert JSON object to Map
        Map<String, String> responseMap = new HashMap<>();
        for (String key : json.keySet()) {
            String priceStr = json.getString(key);
            double price = Double.parseDouble(priceStr);
            String formattedPrice = formatPrice(price);
            responseMap.put(key, formattedPrice);
        }

        return responseMap;
    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(price);
    }


    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        return ResponseEntity.ok("Service is up and running as of " + formattedDateTime);
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
