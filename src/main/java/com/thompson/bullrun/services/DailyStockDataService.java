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

    @PostConstruct
    @Scheduled(cron = "0 0 0 * * 1-6", zone = "America/New_York")
    public void updateStockPrices() {
        log.info("Starting stock price update job");

        List<StockEntity> stocks = stockRepository.findAll();
        for (StockEntity stock : stocks) {
            try {
                String apiUrl = stockPriceURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
                String response = restTemplate.getForObject(apiUrl, String.class);
                JSONObject jsonResponse = new JSONObject(response);
                double price = jsonResponse.getDouble("price");

                // Format the price to two decimal places
                BigDecimal formattedPrice = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
                stock.setClosePrice(formattedPrice.doubleValue());

                // Calculate and format the current value to two decimal places
                BigDecimal currentValue = formattedPrice.multiply(BigDecimal.valueOf(stock.getSharesOwned())).setScale(2, RoundingMode.HALF_UP);
                stock.setCurrentValue(currentValue.doubleValue());

                stock.setTimestamp(LocalDateTime.now());

                // Check if logo image is already present
                if (stock.getLogoImage() == null) {
                    String getLogo = companyLogoURL.replace("{symbol}", stock.getSymbol()).replace("{apiKey}", twelveDataAPIKey);
                    String logoResponse = restTemplate.getForObject(getLogo, String.class); // Fetch logo response
                    JSONObject logoJsonResponse = new JSONObject(logoResponse); // Parse logo response

                    // Download and save the logo image
                    String logoUrl = logoJsonResponse.getString("url");
                    log.info("Logo URL for stock {}: {}", stock.getSymbol(), logoUrl);
                    byte[] logoImage = downloadImage(logoUrl);
                    if (logoImage != null) {
                        log.info("Downloaded logo image for stock {}: {} bytes", stock.getSymbol(), logoImage.length);
                        stock.setLogoImage(logoImage);
                    } else {
                        log.warn("Failed to download logo image for stock {}", stock.getSymbol());
                    }
                } else {
                    log.info("Logo image already exists for stock {}", stock.getSymbol());
                }

                log.info("Updated price for stock: {} to {}. Current value: {}", stock.getSymbol(), formattedPrice, currentValue);
            } catch (Exception e) {
                log.error("Error updating price for stock: {}", stock.getSymbol(), e);
            }
        }
        stockRepository.saveAll(stocks);
        log.info("Stock price update job completed successfully for {} stocks at {}", stocks.size(), LocalDateTime.now());
    }

private byte[] downloadImage(String imageUrl) throws Exception {
    URI uri = new URI(imageUrl);
    URL url = uri.toURL();
    try (InputStream in = url.openStream()) {
        return in.readAllBytes();
    }
}
}