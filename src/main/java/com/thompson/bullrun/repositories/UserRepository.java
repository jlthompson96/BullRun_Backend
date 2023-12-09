package com.thompson.bullrun.repositories;

import com.thompson.bullrun.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String>{
}
