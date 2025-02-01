package com.thompson.bullrun.services;

import com.thompson.bullrun.entities.PortfolioValue;
import com.thompson.bullrun.entities.UserEntity;
import com.thompson.bullrun.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
     *
     * @param userRepository UserRepository object dependency.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a list of all users.
     *
     * @return List of UserEntity objects. Returns an empty list if an exception occurs.
     */
    public List<UserEntity> getUserList() {
        log.info("Attempting to fetch the user list.");
        try {
            List<UserEntity> users = userRepository.findAll();
            log.info("Successfully retrieved {} users.", users.size());
            return users;
        } catch (Exception e) {
            log.error("Failed to retrieve user list.", e);
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a specific user by ID.
     *
     * @param userEntity UserEntity object containing the ID of the user to be fetched.
     * @return UserEntity object if found, otherwise null.
     */
    public UserEntity getUser(UserEntity userEntity) {
        Long userId = Long.valueOf(userEntity.getId());
        log.info("Attempting to fetch user with ID: {}", userId);

        try {
            Optional<UserEntity> user = userRepository.findById(String.valueOf(userId));
            if (user.isPresent()) {
                log.info("User with ID: {} found.", userId);
                return user.get();
            } else {
                log.warn("User with ID: {} not found.", userId);
                return null;
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching user with ID: {}", userId, e);
            return null;
        }
    }

    /**
     * Creates a new user.
     *
     * @param userEntity UserEntity object containing the details of the user to be created.
     * @return Created UserEntity object, or null if an error occurs.
     */
    public UserEntity createUser(UserEntity userEntity) {
        log.info("Attempting to create a new user with username: {}", userEntity);

        try {
            UserEntity createdUser = userRepository.save(userEntity);
            log.info("Successfully created user with ID: {}", createdUser.getId());
            return createdUser;
        } catch (Exception e) {
            log.error("Failed to create user with username: {}", userEntity, e);
            return null;
        }
    }

    public List<PortfolioValue> pullPortfolioValueOverTime() {
        log.info("Pulling portfolio value over time");
        try {
            List<PortfolioValue> portfolioValues = new ArrayList<>();
            List<UserEntity> users = userRepository.findAll();
            for (UserEntity user : users) {
                String date = String.valueOf(user.getDate());
                Double totalValue = user.getTotalValue();
                portfolioValues.add(new PortfolioValue(date, totalValue));
            }
            log.info("Successfully pulled portfolio value over time");
            return portfolioValues;
        } catch (Exception e) {
            log.error("Failed to pull portfolio value over time", e);
            return Collections.emptyList();
        }
    }
}
