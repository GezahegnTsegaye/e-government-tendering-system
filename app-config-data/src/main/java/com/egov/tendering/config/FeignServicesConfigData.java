package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.feign")
public class FeignServicesConfigData {
    private String tenderService;
    private String bidService;
    private String userService;
}