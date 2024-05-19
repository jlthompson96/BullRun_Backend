package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.UserEntity;
import com.thompson.bullrun.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getUserList(){
        try {
            log.info("---- Entering getUserList() ----");
            log.info(userRepository.findAll().toString());
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("---- Error in getUserList() ----");
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public UserEntity getUser(UserEntity userEntity) {
        try {
            log.info("---- Entering getUser() ----");
            return userRepository.findById(userEntity.getId()).orElse(null);
        } catch (Exception e) {
            log.error("---- Error in getUser() ----");
            log.error(e.getMessage());
            return null;
        }
    }

    public UserEntity createUser(UserEntity userEntity) {
        try {
            log.info("---- Entering createUser() ----");
            return userRepository.save(userEntity);
        } catch (Exception e) {
            log.error("---- Error in createUser() ----");
            log.error(e.getMessage());
            return null;
        }
    }
}
