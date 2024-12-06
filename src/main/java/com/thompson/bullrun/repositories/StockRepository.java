package com.thompson.bullrun.repositories;

import com.thompson.bullrun.entities.StockEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StockRepository extends MongoRepository<StockEntity, String> {
    Optional<StockEntity> findBySymbol(String symbol);
    void deleteBySymbol(String symbol);
}
