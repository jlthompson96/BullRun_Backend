package com.thompson.bullrun;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
//
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/stockData").allowedOrigins("http://localhost:5173");
//            }
//        };
//    }

}
