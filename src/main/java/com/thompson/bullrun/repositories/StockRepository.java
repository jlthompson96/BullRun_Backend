package com.thompson.bullrun.repositories;

import com.thompson.bullrun.entities.StockEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockRepository extends MongoRepository<StockEntity, String> {
}
