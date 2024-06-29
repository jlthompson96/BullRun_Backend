package com.thompson.bullrun.common;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * AppConfig is a configuration class that provides application-wide beans.
 */
@Component
public class AppConfig {

    /**
     * Creates a RestTemplate bean that can be used for making HTTP requests.
     *
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

