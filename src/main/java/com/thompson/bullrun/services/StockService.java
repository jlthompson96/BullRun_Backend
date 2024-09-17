package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    public List<StockEntity> getAllStocks() {
        return stockRepository.findAll();
    }

    public StockEntity getStockById(String id) {
        return stockRepository.findById(id).orElse(null);
    }
}