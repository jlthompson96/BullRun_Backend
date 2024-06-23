package com.thompson.bullrun.controller;

import com.thompson.bullrun.entities.UserEntity;
import com.thompson.bullrun.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * UserController is a REST controller that handles HTTP requests related to UserEntity.
 * It uses the UserService to perform operations on UserEntity.
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Handles the GET request to fetch the list of all users.
     * @return ResponseEntity containing the list of UserEntity and HTTP status.
     */
    @RequestMapping("/getUserList")
    public ResponseEntity<List<UserEntity>> getUserList() {
        log.info("---- Entering userList() ----");
        try {
            return new ResponseEntity<>(userService.getUserList(), HttpStatus.OK);
        } catch (Exception e) {
            log.error("---- Error in userList() ----");
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    /**
     * Handles the POST request to fetch a specific user.
     * @param userEntity UserEntity object containing the details of the user to be fetched.
     * @return ResponseEntity containing the UserEntity and HTTP status.
     */
    @PostMapping("/getUser")
    public ResponseEntity<UserEntity> getUser(@RequestBody UserEntity userEntity) {
        log.info("---- Entering getUser() ----");
        try {
            return new ResponseEntity<>(userService.getUser(userEntity), HttpStatus.OK);
        } catch (Exception e) {
            log.error("---- Error in getUser() ----");
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Handles the POST request to create a new user.
     * @param userEntity UserEntity object containing the details of the user to be created.
     * @return ResponseEntity containing the created UserEntity and HTTP status.
     */
    @PostMapping("/createUser")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity userEntity) {
        log.info("---- Entering createUser() ----");
        try {
            return new ResponseEntity<>(userService.createUser(userEntity), HttpStatus.OK);
        } catch (Exception e) {
            log.error("---- Error in createUser() ----");
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}