package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.kafka-config")
public class KafkaConfigData {
    private String bootstrapServers;
    private String schemaRegistryUrl;
    private Topics topics;

    @Data
    public static class Topics {
        private String userService;
        private String employeeService;
        private String notificationService;
    }
}
