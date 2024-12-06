package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.repositories.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockEntity> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<StockEntity> getStockById(String id) {
        return stockRepository.findById(id);
    }

    public void deleteStockBySymbol(String stockId) {
        Optional<StockEntity> stock = stockRepository.findById(stockId);
        if (stock.isPresent()) {
            log.info("Deleting stock with symbol: {}", stock.get().getSymbol());
            stockRepository.deleteById(stockId);
            log.info("Stock with symbol: {} deleted successfully", stock.get().getSymbol());
        } else {
            log.warn("Stock with id: {} not found", stockId);
        }
    }
}