package com.egov.tendering.bidding.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class KafkaDevConfig {
    @Bean
    public NewTopic dummyTopic() {
        // Return a dummy topic just to satisfy bean dependencies
        return new NewTopic("dummy", 1, (short)1);
    }
}