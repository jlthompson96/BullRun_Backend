package com.thompson.bullrun;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class BullRunApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(BullRunApplication.class, args);
            log.info("---------------------------------------");
            log.info("BullRunApplication started successfully");
            log.info("---------------------------------------");
        } catch (Exception e) {
            log.error("Error starting BullRunApplication");
            log.error(e.getMessage());
        }

    }

}
