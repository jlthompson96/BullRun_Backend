package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.repositories.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class DailyStockDataService {

    private final RestTemplate restTemplate;
    private final String stockPriceURL;
    private final String twelveDataAPIKey;
    private final StockRepository stockRepository;

    public DailyStockDataService(RestTemplate restTemplate,
                                 @Value("${stockPrice}") String stockPriceURL,
                                 @Value("${twelveDataAPIKey}") String twelveDataAPIKey,
                                 StockRepository stockRepository) {
        this.restTemplate = restTemplate;
        this.stockPriceURL = stockPriceURL;
        this.twelveDataAPIKey = twelveDataAPIKey;
        this.stockRepository = stockRepository;
    }

    /**
     * Updates the stock prices for all stocks in the database.
     */
    @PostConstruct
    @Scheduled(cron = "0 0 0 * * 1-6", zone = "America/New_York")
    public void updateStockPrices() {
        log.info("Starting stock price update job");

        List<StockEntity> stocks = stockRepository.findAll();
        for (StockEntity stock : stocks) {
            try {
                String apiUrl = stockPriceURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
                String response = restTemplate.getForObject(apiUrl, String.class);
                double price = new JSONObject(response).getDouble("price");

                // Format the price to two decimal places
                BigDecimal formattedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                stock.setClosePrice(formattedPrice.doubleValue());

                // Calculate and format the current value to two decimal places
                BigDecimal currentValue = formattedPrice.multiply(BigDecimal.valueOf(stock.getSharesOwned())).setScale(2, RoundingMode.HALF_UP);
                stock.setCurrentValue(currentValue.doubleValue());

                stock.setTimestamp(LocalDateTime.now());
                log.info("Updated price for stock: {} to {}. Current value: {}", stock.getSymbol(), formattedPrice, currentValue);
            } catch (Exception e) {
                log.error("Error updating price for stock: {}", stock.getSymbol(), e);
            }
        }
        stockRepository.saveAll(stocks);
        log.info("Stock price update job completed successfully for {} stocks at {}", stocks.size(), LocalDateTime.now());
    }
}