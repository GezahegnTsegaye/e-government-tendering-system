package com.egov.tendering.bidding.event;

import com.egov.tendering.bidding.dal.dto.BidDTO;
import com.egov.tendering.bidding.dal.dto.PageDTO;
import com.egov.tendering.bidding.dal.model.BidStatus;
import com.egov.tendering.bidding.service.BidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidEventListener {

    private final BidService bidService;

    /**
     * Listen for tender events to take appropriate actions on bids
     */
    @KafkaListener(topics = "tender-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTenderEvents(@Payload TenderEvent event, Acknowledgment ack) {
        try {
            log.info("Received tender event: type={}, tenderId={}", event.getEventType(), event.getTenderId());

            switch (event.getEventType()) {
                case "TENDER_PUBLISHED" ->
                        log.info("Tender published: {}", event.getTenderId());

                case "TENDER_UPDATED" ->
                        log.info("Tender updated: {}", event.getTenderId());

                case "TENDER_DEADLINE_EXTENDED" ->
                        log.info("Tender deadline extended: {}", event.getTenderId());

                case "TENDER_CLOSED" -> {
                    log.info("Tender closed - updating all bid statuses: {}", event.getTenderId());
                    // Close all draft bids for this tender
                    List<BidDTO> draftBids = bidService.getBidsByTenderAndStatus(event.getTenderId(), BidStatus.DRAFT);
                    for (BidDTO bid : draftBids) {
                        bidService.updateBidStatus(bid.getId(), BidStatus.NOT_SUBMITTED);
                    }
                }

                case "TENDER_CANCELLED" -> {
                    log.info("Tender cancelled - cancelling all bids: {}", event.getTenderId());
                    // Use pagination to handle potentially large number of bids
                    PageDTO<BidDTO> firstPage = bidService.getBidsByTender(event.getTenderId(), PageRequest.of(0, 100));
                    int totalPages = firstPage.getTotalPages();

                    // Process first page
                    processPageForCancellation(firstPage.getContent());

                    // Process any remaining pages
                    for (int i = 1; i < totalPages; i++) {
                        PageDTO<BidDTO> page = bidService.getBidsByTender(
                                event.getTenderId(), PageRequest.of(i, 100));
                        processPageForCancellation(page.getContent());
                    }
                }

                default ->
                        log.warn("Unknown tender event type: {}", event.getEventType());
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing tender event: {}", event, e);
            // Don't acknowledge - will be retried
        }
    }

    // Helper method to process bids for cancellation
    private void processPageForCancellation(List<BidDTO> bids) {
        for (BidDTO bid : bids) {
            if (bid.getStatus() != BidStatus.CANCELLED) {
                bidService.updateBidStatus(bid.getId(), BidStatus.CANCELLED);
            }
        }
    }

    /**
     * Listen for evaluation events to update bid statuses
     */
    @KafkaListener(topics = "evaluation-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEvaluationEvents(@Payload EvaluationEvent event, Acknowledgment ack) {
        try {
            log.info("Received evaluation event: type={}, bidId={}", event.getEventType(), event.getBidId());

            switch (event.getEventType()) {
                case "EVALUATION_STARTED" -> {
                    log.info("Evaluation started for bid: {}", event.getBidId());
                    bidService.updateBidStatus(event.getBidId(), BidStatus.UNDER_EVALUATION);
                }

                case "EVALUATION_COMPLETED" -> {
                    log.info("Evaluation completed for bid: {}", event.getBidId());
                    BidStatus newStatus = "PASS".equals(event.getResult()) ?
                            BidStatus.EVALUATED : BidStatus.REJECTED;
                    bidService.updateBidStatus(event.getBidId(), newStatus);
                }

                case "BID_AWARDED" -> {
                    log.info("Bid awarded: {}", event.getBidId());
                    bidService.updateBidStatus(event.getBidId(), BidStatus.AWARDED);
                }

                default ->
                        log.warn("Unknown evaluation event type: {}", event.getEventType());
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing evaluation event: {}", event, e);
            // Don't acknowledge - will be retried
        }
    }

    /**
     * Listen for contract events to update bid statuses
     */
    @KafkaListener(topics = "contract-events", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeContractEvents(@Payload ContractEvent event, Acknowledgment ack) {
        try {
            log.info("Received contract event: type={}, bidId={}", event.getEventType(), event.getBidId());

            switch (event.getEventType()) {
                case "CONTRACT_CREATED" -> {
                    log.info("Contract created for bid: {}", event.getBidId());
                    bidService.updateBidStatus(event.getBidId(), BidStatus.CONTRACTED);
                }

                case "CONTRACT_SIGNED" ->
                        log.info("Contract signed for bid: {}", event.getBidId());

                case "CONTRACT_TERMINATED" -> {
                    log.info("Contract terminated for bid: {}", event.getBidId());
                    bidService.updateBidStatus(event.getBidId(), BidStatus.TERMINATED);
                }

                default ->
                        log.warn("Unknown contract event type: {}", event.getEventType());
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error processing contract event: {}", event, e);
            // Don't acknowledge - will be retried
        }
    }
}