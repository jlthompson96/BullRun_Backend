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
    private final String stockPriceURL;
    private final String companyLogoURL;
    private final String twelveDataAPIKey;
    private final StockRepository stockRepository;

    public DailyStockDataService(RestTemplate restTemplate,
                                 @Value("${stockPrice}") String stockPriceURL,
                                 @Value("${companyLogo}") String companyLogoURL,
                                 @Value("${twelveDataAPIKey}") String twelveDataAPIKey,
                                 StockRepository stockRepository) {
        this.restTemplate = restTemplate;
        this.stockPriceURL = stockPriceURL;
        this.companyLogoURL = companyLogoURL;
        this.twelveDataAPIKey = twelveDataAPIKey;
        this.stockRepository = stockRepository;
    }

    //    @PostConstruct
    @Scheduled(cron = "0 0 0 * * 1-6", zone = "America/New_York")
    public void updateStockPrices() {
        log.info("Starting stock price update job");

        List<StockEntity> stocks = stockRepository.findAll();
        for (StockEntity stock : stocks) {
            try {
                updateStockPrice(stock);
                updateStockLogo(stock);
                stock.setTimestamp(LocalDateTime.now());
                log.info("Updated price for stock: {} to {}. Current value: {}", stock.getSymbol(), stock.getClosePrice(), stock.getCurrentValue());
            } catch (Exception e) {
                log.error("Error updating price for stock: {}", stock.getSymbol(), e);
            }
        }
        stockRepository.saveAll(stocks);
        log.info("Stock price update job completed successfully for {} stocks at {}", stocks.size(), LocalDateTime.now());
    }

    public void updateOneStockPrice(StockEntity stock) {
        log.info("Starting stock price update for stock: {}", stock.getSymbol());
        try {
            updateStockPriceDetails(stock);
            updateStockLogo(stock);
            stock.setTimestamp(LocalDateTime.now());
            stockRepository.save(stock);
            log.info("Updated price for stock: {} to {}. Current value: {}", stock.getSymbol(), stock.getClosePrice(), stock.getCurrentValue());
        } catch (Exception e) {
            log.error("Error updating price for stock: {}", stock.getSymbol(), e);
        }
    }

    private void updateStockPriceDetails(StockEntity stock) throws Exception {
        String apiUrl = stockPriceURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
        String response = restTemplate.getForObject(apiUrl, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        double price = jsonResponse.getDouble("price");

        BigDecimal formattedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
        stock.setClosePrice(formattedPrice.doubleValue());

        BigDecimal currentValue = formattedPrice.multiply(BigDecimal.valueOf(stock.getSharesOwned())).setScale(2, RoundingMode.HALF_UP);
        stock.setCurrentValue(currentValue.doubleValue());
    }

    private void updateStockPrice(StockEntity stock) throws Exception {
        String apiUrl = stockPriceURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
        String response = restTemplate.getForObject(apiUrl, String.class);
        JSONObject jsonResponse = new JSONObject(response);
        double price = jsonResponse.getDouble("price");

        BigDecimal formattedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
        stock.setClosePrice(formattedPrice.doubleValue());

        BigDecimal currentValue = formattedPrice.multiply(BigDecimal.valueOf(stock.getSharesOwned())).setScale(2, RoundingMode.HALF_UP);
        stock.setCurrentValue(currentValue.doubleValue());
    }

    private void updateStockLogo(StockEntity stock) throws Exception {
            String getLogo = companyLogoURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
            String logoResponse = restTemplate.getForObject(getLogo, String.class);
            JSONObject logoJsonResponse = new JSONObject(logoResponse);

            String logoUrl = logoJsonResponse.getString("url");
            byte[] logoImage = downloadImage(logoUrl);
            stock.setLogoImage(logoImage);
    }

    private byte[] downloadImage(String imageUrl) throws Exception {
        URI uri = new URI(imageUrl);
        URL url = uri.toURL();
        try (InputStream in = url.openStream()) {
            return in.readAllBytes();
        }
    }
}