package com.thompson.bullrun.repositories;

import com.thompson.bullrun.entities.StockEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * StockRepository is an interface that extends MongoRepository.
 * It provides the mechanism for storage, retrieval, and search behavior
 * which emulates a collection of objects.
 */
public interface StockRepository extends MongoRepository<StockEntity, String> {
    Optional<StockEntity> findBySymbol(String symbol);

    void deleteBySymbol(String symbol);
}
