package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.evaluation-service")
public class EvaluationServiceConfigData {
    private String version;
    private String customAudience;
}