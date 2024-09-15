package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.repositories.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Scheduled(cron = "0 0 0 * * ?", zone = "America/New_York")
    public void updateStockPrices() {
        log.info("Starting stock price update job");

        List<StockEntity> stocks = stockRepository.findAll();
        for (StockEntity stock : stocks) {
            try {
                String apiUrl = stockPriceURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
                String response = restTemplate.getForObject(apiUrl, String.class);
                double price = new JSONObject(response).getDouble("price");
                stock.setClosePrice(price);
                stockRepository.save(stock);
                log.info("Updated price for stock: {} to {}", stock.getSymbol(), price);
            } catch (Exception e) {
                log.error("Error updating price for stock: {}", stock.getSymbol(), e);
            }
        }
        log.info("Stock price update job completed");
    }
}