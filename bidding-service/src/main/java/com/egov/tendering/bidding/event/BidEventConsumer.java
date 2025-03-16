package com.egov.tendering.bidding.event;

import com.egov.tendering.bidding.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BidEventConsumer {

    private final BidService bidService;

    /**
     * Listen for tender events to take appropriate actions on bids
     */
    @KafkaListener(topics = "tender-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTenderEvents(@Payload TenderEvent event, Acknowledgment ack) {
        try {
            log.info("Received tender event: {}", event);

            switch (event.getEventType()) {
                case "TENDER_PUBLISHED" -> {
                    log.info("Processing tender published event for tender ID: {}", event.getTenderId());
                    // Potential logic to prepare for new bids for this tender
                }

                case "TENDER_CLOSED" -> {
                    log.info("Processing tender closed event for tender ID: {}", event.getTenderId());
                    bidService.closeBidsForTender(event.getTenderId());
                }

                case "TENDER_CANCELLED" -> {
                    log.info("Processing tender cancelled event for tender ID: {}", event.getTenderId());
                    bidService.cancelBidsForTender(event.getTenderId(), "Tender was cancelled");
                }

                default ->
                        log.warn("Unknown tender event type: {}", event.getEventType());
            }

            // Acknowledge successful processing
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing tender event: {}", event, e);
            // Do not acknowledge - message will be redelivered
        }
    }

    /**
     * Listen for evaluation events to update bid statuses
     */
    @KafkaListener(topics = "evaluation-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEvaluationEvents(@Payload EvaluationEvent event, Acknowledgment ack) {
        try {
            log.info("Received evaluation event: {}", event);

            switch (event.getEventType()) {
                case "BID_EVALUATED" -> {
                    log.info("Processing bid evaluated event for bid ID: {}", event.getBidId());
                    bidService.updateBidEvaluationStatus(
                            event.getBidId(),
                            event.getEvaluationResult(),
                            event.getEvaluatedBy(),
                            event.getComments()
                    );
                }

                case "BID_AWARDED" -> {
                    log.info("Processing bid awarded event for bid ID: {}", event.getBidId());
                    bidService.awardBid(event.getBidId(), event.getAwardedBy(), event.getAwardComments());
                }

                default ->
                        log.warn("Unknown evaluation event type: {}", event.getEventType());
            }

            // Acknowledge successful processing
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing evaluation event: {}", event, e);
            // Do not acknowledge - message will be redelivered
        }
    }

    /**
     * Listen for contract events to update bid statuses
     */
    @KafkaListener(topics = "contract-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeContractEvents(@Payload ContractEvent event, Acknowledgment ack) {
        try {
            log.info("Received contract event: {}", event);

            if ("CONTRACT_CREATED".equals(event.getEventType())) {
                log.info("Processing contract created event for bid ID: {}", event.getBidId());
                bidService.updateBidContractStatus(event.getBidId(), event.getContractId());
            } else {
                log.warn("Unknown contract event type: {}", event.getEventType());
            }

            // Acknowledge successful processing
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing contract event: {}", event, e);
            // Do not acknowledge - message will be redelivered
        }
    }
}