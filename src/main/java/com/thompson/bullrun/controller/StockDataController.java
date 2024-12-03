package com.thompson.bullrun.controller;

import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.services.DailyStockDataService;
import com.thompson.bullrun.services.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@Tag(name = "Stock Data Controller", description = "Endpoints for fetching stock data")
@RequestMapping("/stockData")
public class StockDataController {

    private final RestTemplate restTemplate;
    private final String twelveDataAPIKey;
    private final String polygonAPIKey;
    private final String stockPriceURL;
    private final String companyProfileURL;
    private final String previousCloseURL;
    private final String companyLogoURL;

    @Autowired
    public StockDataController(RestTemplate restTemplate,
                               @Value("${twelveDataAPIKey}") String twelveDataAPIKey,
                               @Value("${polygonAPIKey}") String polygonAPIKey,
                               @Value("${stockPrice}") String stockPriceURL,
                               @Value("${companyProfile}") String companyProfileURL,
                               @Value("${previousClose}") String previousCloseURL,
                               @Value("${companyLogo}") String companyLogoURL, StockService stockService, DailyStockDataService dailyStockDataService) {
        this.restTemplate = restTemplate;
        this.twelveDataAPIKey = twelveDataAPIKey;
        this.polygonAPIKey = polygonAPIKey;
        this.stockPriceURL = stockPriceURL;
        this.companyProfileURL = companyProfileURL;
        this.previousCloseURL = previousCloseURL;
        this.companyLogoURL = companyLogoURL;
        this.stockService = stockService;
        this.dailyStockDataService = dailyStockDataService;
    }

    private final String[] indexSymbols = {"DJI", "SPX", "IXIC"};
    private final String[] indexNames = {"Dow Jones Industrial Average", "S&P 500", "Nasdaq Composite"};
    private final StockService stockService;
    private final DailyStockDataService dailyStockDataService;

    @GetMapping("/companyProfile")
    public ResponseEntity<String> getCompanyProfile(@RequestParam String symbol) {
        log.info("Fetching company profile for symbol: {}", symbol);
        ResponseEntity<String> response = fetchData(companyProfileURL, symbol, polygonAPIKey);
        logResponseStatus("Company Profile", symbol, response);
        return response;
    }

    @GetMapping("/stockPrice")
    public ResponseEntity<String> getStockPrice(@RequestParam String symbol) {
        log.info("Fetching stock price for symbol: {}", symbol);
        ResponseEntity<String> response = fetchData(stockPriceURL, symbol, twelveDataAPIKey);
        logResponseStatus("Stock Price", symbol, response);
        return response;
    }

    @GetMapping("/companyLogo")
    public ResponseEntity<String> getCompanyLogo(@RequestParam String symbol) {
        log.info("Fetching company logo for symbol: {}", symbol);
        ResponseEntity<String> response = fetchData(companyLogoURL, symbol, twelveDataAPIKey);
        logResponseStatus("Company Logo", symbol, response);
        return response;
    }

    @GetMapping("/previousClose")
    public ResponseEntity<String> getPreviousClose(@RequestParam String symbol) {
        log.info("Fetching previous close for symbol: {}", symbol);
        ResponseEntity<String> response = fetchData(previousCloseURL, symbol, twelveDataAPIKey);
        logResponseStatus("Previous Close", symbol, response);
        return response;
    }

    @GetMapping("/stockNews")
    public ResponseEntity<String> getRSSFeed(@RequestParam String symbol) {
        log.info("Fetching RSS feed for symbol: {}", symbol);
        try {
            final String url = "https://feeds.finance.yahoo.com/rss/2.0/headline?s=" + symbol;
            String rssFeed = restTemplate.getForObject(url, String.class);

            if (rssFeed == null) {
                log.warn("RSS feed returned null for symbol: {}", symbol);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RSS feed is unavailable.");
            }

            JSONObject json = XML.toJSONObject(rssFeed);
            JSONArray items = json.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

            log.info("Successfully fetched RSS feed items for symbol: {}", symbol);
            return ResponseEntity.ok(items.toString());
        } catch (Exception e) {
            log.error("Error occurred while fetching RSS feed for symbol: {}", symbol, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving RSS feed");
        }
    }

    @GetMapping("/indexPrices")
    public Map<String, String> getIndexPrices() {
        log.info("Fetching index prices for major indices");
        List<String> indexSymbols = Arrays.asList("DJI", "SPX", "IXIC");

        Map<String, String> indexPrices = indexSymbols.stream()
                .collect(Collectors.toMap(symbol -> symbol, this::getFormattedPrice));

        log.info("Index prices fetched successfully: {}", indexPrices);
        return indexPrices;
    }

    @Operation(summary = "Adds a new stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock added successfully",
                    content = @Content(schema = @Schema(implementation = StockEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addStock")
    public ResponseEntity<StockEntity> addStock(@RequestBody StockEntity stockEntity) {
        log.info("Adding new stock with symbol: {}", stockEntity.getSymbol());
        try {
            dailyStockDataService.updateOneStockPrice(stockEntity);
//            StockEntity createdStock = stockService.addStock(stockEntity);
            log.info("Successfully added new stock with id: {}", stockEntity.getId());
            return new ResponseEntity<>(stockEntity, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to add new stock", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String getFormattedPrice(String symbol) {
        log.debug("Fetching and formatting price for index symbol: {}", symbol);
        ResponseEntity<String> response = fetchData(stockPriceURL, symbol, twelveDataAPIKey);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            String priceStr = new JSONObject(response.getBody()).getString("price");
            double price = Double.parseDouble(priceStr);
            String formattedPrice = formatPrice(price);
            log.debug("Formatted price for symbol {}: {}", symbol, formattedPrice);
            return formattedPrice;
        } else {
            log.warn("Failed to fetch price for symbol {}. Status: {}", symbol, response.getStatusCode());
            return "N/A";
        }
    }

    private String formatPrice(double price) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        String formattedPrice = decimalFormat.format(price);
        log.debug("Formatted price: {}", formattedPrice);
        return formattedPrice;
    }

    private ResponseEntity<String> fetchData(String url, String symbol, String apiKey) {
        log.info("Fetching data for symbol: {} from endpoint: {}", symbol, url);
        try {
            String apiUrl = constructApiUrl(url, symbol, apiKey);
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched data for symbol: {}", symbol);
            } else {
                log.warn("Received non-2xx response for symbol: {}. Status: {}", symbol, response.getStatusCode());
            }
            return response;
        } catch (HttpClientErrorException e) {
            log.error("Client error while fetching data for symbol: {}. Status code: {}", symbol, e.getStatusCode(), e);
            return ResponseEntity.status(e.getStatusCode()).body("Error retrieving data");
        } catch (ResourceAccessException e) {
            log.error("Resource access error while fetching data for symbol: {}", symbol, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving data");
        }
    }

    private String constructApiUrl(String endpoint, String symbol, String apiKey) {
        String apiUrl = endpoint.replace("{apiKey}", apiKey).replace("{symbol}", symbol);
        log.debug("Constructed API URL: {}", apiUrl);
        return apiUrl;
    }

    private void logResponseStatus(String dataType, String symbol, ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("{} data successfully fetched for symbol: {}", dataType, symbol);
        } else {
            log.warn("Failed to fetch {} data for symbol: {}. Status: {}", dataType, symbol, response.getStatusCode());
        }
    }
}