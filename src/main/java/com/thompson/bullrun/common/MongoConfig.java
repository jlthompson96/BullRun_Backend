package com.thompson.bullrun.common;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new com.mongodb.ConnectionString("mongodb+srv://bullrundev:saztAp-4figmo-byjqop@bullrundev.sexuf.mongodb.net/?retryWrites=true&w=majority&appName=BullRunDev"))
                .addCommandListener(new MongoCommandListener())
                .build();
        return MongoClients.create(settings);
    }
}