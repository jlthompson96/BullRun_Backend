package com.thompson.bullrun.controller;

import com.thompson.bullrun.entities.StockEntity;
import com.thompson.bullrun.entities.UserEntity;
import com.thompson.bullrun.services.StockService;
import com.thompson.bullrun.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
    @Autowired
    StockService stockService;

    /**
     * Handles the GET request to fetch the list of all users.
     *
     * @return ResponseEntity containing the list of UserEntity and HTTP status.
     */
    @Operation(summary = "Fetches the list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users fetched successfully",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
     *
     * @param userEntity UserEntity object containing the details of the user to be fetched.
     * @return ResponseEntity containing the UserEntity and HTTP status.
     */
    @Operation(summary = "Fetches a specific user", description = "Retrieves details of a specific user based on the provided user entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fetched successfully",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
     *
     * @param userEntity UserEntity object containing the details of the user to be created.
     * @return ResponseEntity containing the created UserEntity and HTTP status.
     */
    @Operation(summary = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    
@GetMapping("/getUserStocks")
public ResponseEntity<List<StockEntity>> getUserStocks() {
    log.info("---- Entering getUserStocks() ----");
    try {
        List<StockEntity> stocks = stockService.getAllStocks();
        log.info("Stocks returned: {}", stocks.toString());
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    } catch (Exception e) {
        log.error("---- Error in getUserStocks() ----");
        log.error(e.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
}