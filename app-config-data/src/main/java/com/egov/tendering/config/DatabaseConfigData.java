package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.database")
public class DatabaseConfigData {
    private Integer defaultPoolSize;
    private Integer maxPoolSize;
    private Long idleTimeout;
}
