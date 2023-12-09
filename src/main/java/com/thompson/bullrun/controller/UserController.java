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

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

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
