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
import java.util.function.Supplier;

/**
 * UserController is a REST controller that handles HTTP requests related to UserEntity.
 * It uses the UserService to perform operations on UserEntity.
 */
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final StockService stockService;

    @Autowired
    public UserController(UserService userService, StockService stockService) {
        this.userService = userService;
        this.stockService = stockService;
    }

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
    @GetMapping("/getUserList")
    public ResponseEntity<List<UserEntity>> getUserList() {
        log.info("Fetching the list of all users");
        return processRequest(userService::getUserList, "user list");
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
        log.info("Fetching user details for username: {}", userEntity);
        return processRequest(() -> userService.getUser(userEntity), "user details");
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
        log.info("Creating new user with username: {}", userEntity);
        return processRequest(() -> userService.createUser(userEntity), "new user");
    }

    /**
     * Handles the GET request to fetch all stocks associated with a user.
     *
     * @return ResponseEntity containing the list of StockEntity and HTTP status.
     */
    @Operation(summary = "Fetches all stocks associated with the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stocks fetched successfully",
                    content = @Content(schema = @Schema(implementation = StockEntity.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getUserStocks")
    public ResponseEntity<List<StockEntity>> getUserStocks() {
        log.info("Fetching all stocks for user");
        return processRequest(stockService::getAllStocks, "stocks list");
    }

    /**
     * Utility method to process a request and handle exceptions consistently.
     *
     * @param action   Supplier that provides the data to be returned in ResponseEntity.
     * @param dataType A descriptive string for the data being fetched, used in logging.
     * @param <T>      Type of data expected in the response.
     * @return ResponseEntity containing the data and HTTP status.
     */
    private <T> ResponseEntity<T> processRequest(Supplier<T> action, String dataType) {
        try {
            T data = action.get();
            log.info("Successfully fetched {}", dataType);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to fetch {}", dataType, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
