package com.egov.tendering.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
public class KafkaProducerConfigData {
    private String keySerializer = "org.apache.kafka.common.serialization.StringSerializer";
    private String valueSerializer = "org.springframework.kafka.support.serializer.JsonSerializer";
    private String acks = "all";
    private Integer batchSize = 16384;
    private Integer lingerMs = 5;
    private Integer requestTimeoutMs = 30000;
    private Integer retryCount = 5;
    private Integer compressionType = 1; // 1 represents GZIP
    private String compressionCodec = "gzip";
}
