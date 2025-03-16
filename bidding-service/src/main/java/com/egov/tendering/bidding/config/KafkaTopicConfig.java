package com.egov.tendering.bidding.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${app.kafka.topics.bid-events}")
    private String bidEventsTopic;

    /**
     * Creates the bid events topic with partitions for parallelism
     * and replication factor for reliability
     */
    @Bean
    public NewTopic bidEventsTopic() {
        return TopicBuilder.name(bidEventsTopic)
                .partitions(3)
                .replicas(1)  // Set to higher value in production
                .build();
    }

    /**
     * Topic for tender events that bidding service needs to listen to
     */
    @Bean
    public NewTopic tenderEventsTopic() {
        return TopicBuilder.name("tender-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Topic for evaluation events that bidding service needs to listen to
     */
    @Bean
    public NewTopic evaluationEventsTopic() {
        return TopicBuilder.name("evaluation-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    /**
     * Topic for contract events that bidding service needs to listen to
     */
    @Bean
    public NewTopic contractEventsTopic() {
        return TopicBuilder.name("contract-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}