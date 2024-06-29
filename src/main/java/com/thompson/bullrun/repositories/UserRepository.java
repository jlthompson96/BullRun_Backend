package com.thompson.bullrun.repositories;

import com.thompson.bullrun.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * UserRepository is an interface that extends MongoRepository.
 * It provides the mechanism for storage, retrieval, and search behavior
 * which emulates a collection of objects.
 *
 * @author jlthompson96
 * @version 1.0
 * @since 2024.1.4
 */
public interface UserRepository extends MongoRepository<UserEntity, String>{
}