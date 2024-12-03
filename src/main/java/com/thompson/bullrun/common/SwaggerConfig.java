package com.thompson.bullrun.common;

import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Swagger documentation for the API.
 * Uses SpringDoc's GroupedOpenApi to group and document API endpoints.
 */
@Slf4j
@Configuration
public class SwaggerConfig {

    private static final String API_GROUP_NAME = "com.thompson.bullrun";
    private static final String API_PATHS = "/**";

    /**
     * Configures the Swagger API documentation group.
     *
     * @return GroupedOpenApi instance that configures the documentation for all API endpoints under the specified group.
     */
    @Bean
    public GroupedOpenApi publicApi() {
        log.info("Initializing Swagger configuration for API documentation...");

        GroupedOpenApi groupedApi = GroupedOpenApi.builder()
                .group(API_GROUP_NAME)
                .pathsToMatch(API_PATHS)
                .build();

        log.info("Swagger configuration initialized with group: '{}' and paths: '{}'", API_GROUP_NAME, API_PATHS);

        return groupedApi;
    }
}
