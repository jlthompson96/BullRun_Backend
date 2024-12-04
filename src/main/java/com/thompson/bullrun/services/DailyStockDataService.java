package com.thompson.bullrun.services;

import com.thompson.bullrun.config.ApiProperties;
import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.exceptions.StockDataException;
import com.thompson.bullrun.repositories.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DailyStockDataService {

    private final RestTemplate restTemplate;
    private final ApiProperties apiProperties;
    private final StockRepository stockRepository;

    public DailyStockDataService(RestTemplate restTemplate, ApiProperties apiProperties, StockRepository stockRepository) {
        this.restTemplate = restTemplate;
        this.apiProperties = apiProperties;
        this.stockRepository = stockRepository;
    }

//    @PostConstruct
    @Scheduled(cron = "0 0 0 * * 1-6", zone = "America/New_York")
    public void updateStockPrices() {
        LocalDateTime startTime = LocalDateTime.now();
        log.info("Starting stock price update job at {}", startTime);

        List<StockEntity> stocks = stockRepository.findAll();
        for (StockEntity stock : stocks) {
            try {
                updateStockPrice(stock);
                updateStockLogo(stock);
                stock.setTimestamp(LocalDateTime.now());
                log.info("Updated price for stock: {} to {}. Current value: {}", stock.getSymbol(), stock.getClosePrice(), stock.getCurrentValue());
            } catch (StockDataException e) {
                log.error("Error updating price for stock: {}", stock.getSymbol(), e);
            }
        }
        stockRepository.saveAll(stocks);
        LocalDateTime endTime = LocalDateTime.now();
        log.info("Stock price update job completed successfully for {} stocks at {}. Duration: {} seconds", stocks.size(), endTime, java.time.Duration.between(startTime, endTime).getSeconds());
    }

    public void updateOneStockPrice(StockEntity stock) {
        log.info("Starting stock price update for stock: {}", stock.getSymbol());
        try {
            updateStockPrice(stock);
            updateStockLogo(stock);
            stock.setTimestamp(LocalDateTime.now());
            stockRepository.save(stock);
            log.info("Updated price for stock: {} to {}. Current value: {}", stock.getSymbol(), stock.getClosePrice(), stock.getCurrentValue());
        } catch (StockDataException e) {
            log.error("Error updating price for stock: {}", stock.getSymbol(), e);
        }
    }

    private void updateStockPrice(StockEntity stock) throws StockDataException {
        log.debug("Updating stock price for symbol: {}", stock.getSymbol());
        try {
            String apiUrl = apiProperties.getStockPriceURL().replace("{symbol}", stock.getSymbol()).replace("{apiKey}", apiProperties.getTwelveDataAPIKey());
            String response = restTemplate.getForObject(apiUrl, String.class);
            JSONObject jsonResponse = new JSONObject(response);
            double price = jsonResponse.getDouble("price");


            BigDecimal formattedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
            stock.setClosePrice(formattedPrice.doubleValue());

            BigDecimal currentValue = formattedPrice.multiply(BigDecimal.valueOf(stock.getSharesOwned())).setScale(2, RoundingMode.HALF_UP);
            stock.setCurrentValue(currentValue.doubleValue());

            log.debug("Updated stock price for symbol: {} to {}", stock.getSymbol(), formattedPrice);
        } catch (Exception e) {
            throw new StockDataException("Failed to update stock price for symbol: " + stock.getSymbol(), e);
        }
    }

    private void updateStockLogo(StockEntity stock) throws StockDataException {
        log.debug("Updating stock logo for symbol: {}", stock.getSymbol());
        try {
            String getLogo = apiProperties.getCompanyLogoURL().replace("{symbol}", stock.getSymbol()).replace("{apiKey}", apiProperties.getTwelveDataAPIKey());
            String logoResponse = restTemplate.getForObject(getLogo, String.class);
            JSONObject logoJsonResponse = new JSONObject(logoResponse);

            String logoUrl = logoJsonResponse.getString("url");
            byte[] logoImage = downloadImage(logoUrl);
            stock.setLogoImage(logoImage);

            log.debug("Updated stock logo for symbol: {}", stock.getSymbol());
        } catch (Exception e) {
            throw new StockDataException("Failed to update stock logo for symbol: " + stock.getSymbol(), e);
        }
    }

    private byte[] downloadImage(String imageUrl) throws StockDataException {
        log.debug("Downloading image from URL: {}", imageUrl);
        try {
            URI uri = new URI(imageUrl);
            URL url = uri.toURL();
            try (InputStream in = url.openStream()) {
                byte[] imageBytes = in.readAllBytes();
                log.debug("Downloaded image from URL: {}", imageUrl);
                return imageBytes;
            }
        } catch (Exception e) {
            throw new StockDataException("Failed to download image from URL: " + imageUrl, e);
        }
    }
}