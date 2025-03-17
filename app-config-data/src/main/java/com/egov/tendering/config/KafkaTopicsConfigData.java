package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.kafka.topics")
public class KafkaTopicsConfigData {
    private String evaluationCreated;
    private String evaluationUpdated;
    private String evaluationStatusChanged;
    private String evaluationDeleted;
    private String tenderEvaluationCompleted;
    private String tenderEvaluationApproved;
    private String reviewCreated;
    private String reviewUpdated;
    private String reviewDeleted;
}
