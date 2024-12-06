package com.thompson.bullrun.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
public class ApiProperties {
    @Value("${twelveDataAPIKey}")
    private String twelveDataAPIKey;
    @Value("${polygonAPIKey}")
    private String polygonAPIKey;
    @Value("${stockPrice}")
    private String stockPriceURL;
    @Value("${companyProfile}")
    private String companyProfileURL;
    @Value("${previousClose}")
    private String previousCloseURL;
    @Value("${companyLogo}")
    private String companyLogoURL;
}