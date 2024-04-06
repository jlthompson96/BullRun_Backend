package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/stockData")
public class StockDataController {

    private final RestTemplate restTemplate;
    private final String twelveDataAPIKey;
    private final String polygonAPIKey;
    private final String stockPriceURL;
    private final String companyProfileURL;
    private final String companyLogoURL;
    private final String previousCloseURL;

    @Autowired
    public StockDataController(RestTemplate restTemplate,
                               @Value("${twelveDataAPIKey}") String twelveDataAPIKey,
                               @Value("${polygonAPIKey}") String polygonAPIKey,
                               @Value("${stockPrice}") String stockPriceURL,
                               @Value("${companyProfile}") String companyProfileURL,
                               @Value("${companyLogo}") String companyLogoURL,
                               @Value("${previousClose}") String previousCloseURL) {
        this.restTemplate = restTemplate;
        this.twelveDataAPIKey = twelveDataAPIKey;
        this.polygonAPIKey = polygonAPIKey;
        this.stockPriceURL = stockPriceURL;
        this.companyProfileURL = companyProfileURL;
        this.companyLogoURL = companyLogoURL;
        this.previousCloseURL = previousCloseURL;
    }

    @GetMapping("/companyProfile")
    public ResponseEntity<String> getCompanyProfile(@RequestParam String symbol) {
        log.info("Getting company profile for symbol: {}", symbol);
        return fetchData(companyProfileURL, symbol, polygonAPIKey);
    }

    @GetMapping("/companyLogo")
    public ResponseEntity<String> getCompanyLogo(@RequestParam String symbol) {
        log.info("Getting company logo for symbol: {}", symbol);
        return fetchData(companyLogoURL, symbol, twelveDataAPIKey);
    }

    @GetMapping("/stockPrice")
    public ResponseEntity<String> getStockPrice(@RequestParam String symbol) {
        log.info("Getting stock price for symbol: {}", symbol);
        return fetchData(stockPriceURL, symbol, twelveDataAPIKey);
    }

    @GetMapping("/previousClose")
    public ResponseEntity<String> getPreviousClose(@RequestParam String symbol) {
        log.info("Getting previous close for symbol: {}", symbol);
        return fetchData(previousCloseURL, symbol, twelveDataAPIKey);
    }

    @GetMapping("/stockNews")
    public ResponseEntity<String> getRSSFeed(@RequestParam String symbol) {
        log.info("Getting RSS feed for symbol: {}", symbol);
        final String url = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=" + symbol;
        RestTemplate restTemplate = new RestTemplate();
        String rssFeed = restTemplate.getForObject(url, String.class);

        assert rssFeed != null;
        JSONObject json = XML.toJSONObject(rssFeed);
        JSONArray items = json.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

        log.info("RSS feed for symbol: {} retrieved successfully", symbol);
        log.info("RSS feed: {}", items);
        return ResponseEntity.ok(items.toString());
    }

    @GetMapping("/indexPrices")
    public Map<String, String> getIndexPrices() {
        log.info("Getting index prices");
        ResponseEntity<String> djiPrice = fetchData(stockPriceURL, "DJI", twelveDataAPIKey);
        ResponseEntity<String> spxPrice = fetchData(stockPriceURL, "SPX", twelveDataAPIKey);
        ResponseEntity<String> ndxPrice = fetchData(stockPriceURL, "IXIC", twelveDataAPIKey);

        JSONObject json = new JSONObject();
        json.put("DJI", new JSONObject(Objects.requireNonNull(djiPrice.getBody())).getString("price"));
        json.put("SP500", new JSONObject(Objects.requireNonNull(spxPrice.getBody())).getString("price"));
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

    String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(price);
    }


    ResponseEntity<String> fetchData(String url, String symbol, String apiKey) {
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
