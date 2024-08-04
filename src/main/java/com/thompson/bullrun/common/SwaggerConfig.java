package com.thompson.bullrun.common;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("com.thompson.bullrun")
                .pathsToMatch("/**")
                .build();
    }
}
