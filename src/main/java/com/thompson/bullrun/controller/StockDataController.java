
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
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
        return fetchData(companyProfileURL, symbol, twelveDataAPIKey);
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
        List<String> indexSymbols = Arrays.asList("DJI", "SPX", "IXIC");
        Map<String, String> responseMap = new HashMap<>();

        for (String symbol : indexSymbols) {
            ResponseEntity<String> responseEntity = fetchData(stockPriceURL, symbol, twelveDataAPIKey);
            String priceStr = new JSONObject(Objects.requireNonNull(responseEntity.getBody())).getString("price");
            double price = Double.parseDouble(priceStr);
            String formattedPrice = formatPrice(price);
            responseMap.put(symbol, formattedPrice);
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
        log.info("Fetching data for symbol: {}", symbol);
        try {
            String apiUrl = constructApiUrl(url, symbol, apiKey);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched data for symbol: {}", symbol);
                return responseEntity;
            } else {
                log.error("Error fetching data for symbol: {}. Status code: {}", symbol, responseEntity.getStatusCode());
                return ResponseEntity.status(responseEntity.getStatusCode()).body("Error retrieving data");
            }
        } catch (HttpClientErrorException | ResourceAccessException e) {
            log.error("Exception occurred while fetching data for symbol: {}", symbol, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving data");
        }
    }

    private String constructApiUrl(String endpoint, String symbol, String apiKey) {
        return endpoint.replace("{apiKey}", apiKey).replace("{symbol}", symbol);
    }
}