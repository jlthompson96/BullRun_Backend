package com.thompson.bullrun.controller;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * This is a controller class for handling stock data related requests.
 * It uses RestTemplate to make HTTP requests to external APIs for fetching stock data.
 */
@Slf4j
@RestController
@RequestMapping("/stockData")
public class StockDataController {

    private final RestTemplate restTemplate;
    private final String twelveDataAPIKey;
    private final String polygonAPIKey;
    private final String stockPriceURL;
    private final String companyProfileURL;
    private final String previousCloseURL;
    /**
     * Constructor for StockDataController.
     * It initializes the RestTemplate and API keys for external services.
     * @param restTemplate The RestTemplate to make HTTP requests.
     * @param twelveDataAPIKey The API key for the Twelve Data API.
     * @param polygonAPIKey The API key for the Polygon API.
     * @param stockPriceURL The URL for fetching stock price data.
     * @param companyProfileURL The URL for fetching company profile data.
     * @param previousCloseURL The URL for fetching previous close data.
     *
     */
    @Autowired
    public StockDataController(RestTemplate restTemplate,
                               @Value("${twelveDataAPIKey}") String twelveDataAPIKey,
                               @Value("${polygonAPIKey}") String polygonAPIKey,
                               @Value("${stockPrice}") String stockPriceURL,
                               @Value("${companyProfile}") String companyProfileURL,
                               @Value("${previousClose}") String previousCloseURL) {
        this.restTemplate = restTemplate;
        this.twelveDataAPIKey = twelveDataAPIKey;
        this.polygonAPIKey = polygonAPIKey;
        this.stockPriceURL = stockPriceURL;
        this.companyProfileURL = companyProfileURL;
        this.previousCloseURL = previousCloseURL;
    }

    /**
     * This method fetches the company profile for a given stock symbol.
     *
     * @param symbol The stock symbol to fetch the company profile for.
     * @return ResponseEntity containing the company profile data or an error message.
     */
    @GetMapping("/companyProfile")
    public ResponseEntity<String> getCompanyProfile(@RequestParam String symbol) {
        log.info("Getting company profile for symbol: {}", symbol);
        return fetchData(companyProfileURL, symbol, polygonAPIKey);
    }

    /**
     * This method fetches the current stock price for a given stock symbol.
     *
     * @param symbol The stock symbol to fetch the stock price for.
     * @return ResponseEntity containing the stock price data or an error message.
     */
    @GetMapping("/stockPrice")
    public ResponseEntity<String> getStockPrice(@RequestParam String symbol) {
        log.info("Getting stock price for symbol: {}", symbol);
        return fetchData(stockPriceURL, symbol, twelveDataAPIKey);
    }

    /**
     * This method fetches the previous closing price for a given stock symbol.
     *
     * @param symbol The stock symbol to fetch the previous closing price for.
     * @return ResponseEntity containing the previous closing price data or an error message.
     */
    @GetMapping("/previousClose")
    public ResponseEntity<String> getPreviousClose(@RequestParam String symbol) {
        log.info("Getting previous close for symbol: {}", symbol);
        return fetchData(previousCloseURL, symbol, twelveDataAPIKey);
    }

    /**
     * This method fetches the open and close price for a given stock symbol.
     *
     * @param symbol The stock symbol to fetch the open and close price for.
     * @return ResponseEntity containing the open and close price data or an error message.
     */
//    @GetMapping("/openClosePrice")
//    public ResponseEntity<String> getOpenClosePrice(@RequestParam String symbol) {
//        log.info("Getting open and close price for symbol: {}", symbol);
//        return fetchData(openClosePriceURL, symbol, polygonAPIKey);
//    }

    /**
     * This method fetches the company profile, open and close price for a list of stock symbols.
     *
     * @param symbols List of stock symbols to fetch the data for.
     * @return ResponseEntity containing the aggregated data for the list of stock symbols.
     */
//    @GetMapping("/bulkStockData")
//    public ResponseEntity<Map<String, Object>> getBulkStockData(@RequestParam List<String> symbols) {
//        Map<String, Object> aggregatedData = new HashMap<>();
//        for (String symbol : symbols) {
//            Map<String, Object> symbolData = new HashMap<>();
//
//            // Fetch and process company profile
//            ResponseEntity<String> companyProfileResponse = getCompanyProfile(symbol);
//            if (companyProfileResponse.getStatusCode().is2xxSuccessful()) {
//                String companyName = new JSONObject(companyProfileResponse.getBody()).getJSONObject("results").getString("name");
//                int classIndex = companyName.indexOf("Class");
//                int commonIndex = companyName.indexOf("Common");
//                if (classIndex != -1) {
//                    companyName = companyName.substring(0, classIndex).trim();
//                } else if (commonIndex != -1) {
//                    companyName = companyName.substring(0, commonIndex).trim();
//                }
//                symbolData.put("companyName", companyName);
//            } else {
//                symbolData.put("companyName", "Data not available");
//            }
//
//            // Fetch and process stock price
//            ResponseEntity<String> stockPriceResponse = getOpenClosePrice(symbol);
//            if (stockPriceResponse.getStatusCode().is2xxSuccessful()) {
//                symbolData.put("openPrice", new JSONObject(stockPriceResponse.getBody()).getJSONArray("results").getJSONObject(0).getBigDecimal("o"));
//                symbolData.put("previousClose", new JSONObject(stockPriceResponse.getBody()).getJSONArray("results").getJSONObject(0).getBigDecimal("c"));
//            } else {
//                symbolData.put("openPrice", "Data not available");
//                symbolData.put("previousClose", "Data not available");
//            }
//
//            aggregatedData.put(symbol, symbolData);
//            log.info("Aggregated data for symbol: {}", symbol);
//            log.info("Company name: {}", symbolData.get("companyName"));
//            log.info("Open price: {}", symbolData.get("openPrice"));
//            log.info("Previous close: {}", symbolData.get("previousClose"));
//            log.info("-------------------------------------------------");
//        }
//        return ResponseEntity.ok(aggregatedData);
//    }

    /**
     * This method fetches the RSS feed for a given stock symbol.
     *
     * @param symbol The stock symbol to fetch the RSS feed for.
     * @return ResponseEntity containing the RSS feed data or an error message.
     */
    @GetMapping("/stockNews")
    public ResponseEntity<String> getRSSFeed(@RequestParam String symbol) {
        try {
            log.info("Getting RSS feed for symbol: {}", symbol);
            final String url = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=" + symbol;
            RestTemplate restTemplate = new RestTemplate();
            String rssFeed = restTemplate.getForObject(url, String.class);

            assert rssFeed != null;
            JSONObject json = XML.toJSONObject(rssFeed);
            JSONArray items = json.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

            log.info("RSS feed for symbol: {} retrieved successfully", symbol);
            log.info("RSS feed: {}", items);
            return ResponseEntity.ok().body(items.toString());
        } catch (Exception e) {
            log.error("Error occurred while getting RSS feed for symbol: {}", symbol, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving RSS feed");
        }
    }

    /**
     * This method fetches the current prices for the major stock indices.
     *
     * @return Map containing the current prices for the major stock indices.
     */
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

    /**
     * This method formats the price to include commas and two decimal places.
     *
     * @param price The price to format.
     * @return The formatted price.
     */
    String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat.format(price);
    }

    /**
     * This method fetches data from an external API for a given stock symbol.
     *
     * @param url    The URL of the external API.
     * @param symbol The stock symbol to fetch data for.
     * @param apiKey The API key for the external service.
     * @return ResponseEntity containing the data or an error message.
     */
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

    /**
     * This method constructs the API URL by replacing placeholders with actual values.
     *
     * @param endpoint The endpoint URL with placeholders.
     * @param symbol   The stock symbol to fetch data for.
     * @param apiKey   The API key for the external service.
     * @return The constructed API URL.
     */
    private String constructApiUrl(String endpoint, String symbol, String apiKey) {
        return endpoint.replace("{apiKey}", apiKey).replace("{symbol}", symbol);
    }
}