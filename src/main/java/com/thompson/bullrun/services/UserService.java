package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.UserEntity;
import com.thompson.bullrun.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * UserService is a service class that provides methods to interact with the UserRepository.
 * It provides methods to get a list of users, get a specific user, and create a user.
 */
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * @param userRepository UserRepository object dependency.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * This method is used to get a list of all users.
     * @return List of UserEntity objects. Returns an empty list if an exception occurs.
     */
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

    /**
     * This method is used to get a specific user.
     * @param userEntity UserEntity object with the id of the user to be fetched.
     * @return UserEntity object. Returns null if an exception occurs or if the user is not found.
     */
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

    /**
     * This method is used to create a new user.
     * @param userEntity UserEntity object with the details of the user to be created.
     * @return UserEntity object. Returns null if an exception occurs.
     */
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