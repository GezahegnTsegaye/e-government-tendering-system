package com.egov.tendering.audit.event;

import com.egov.tendering.audit.dal.model.AuditLog;
import com.egov.tendering.audit.dal.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventConsumer {

    private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "tender-events", groupId = "${spring.application.name}")
    public void listenTenderEvents(Object event) {
        log.info("Received tender event: {}", event);
        processEvent(event, "Tender", "Tenderee");
    }

    @KafkaListener(topics = "evaluation-events", groupId = "${spring.application.name}")
    public void listenEvaluationEvents(Object event) {
        log.info("Received evaluation event: {}", event);
        processEvent(event, "TenderOffer", "Evaluator");
    }

    @KafkaListener(topics = "contract-events", groupId = "${spring.application.name}")
    public void listenContractEvents(Object event) {
        log.info("Received contract event: {}", event);
        processEvent(event, "Contract", "Committee");
    }

    private void processEvent(Object event, String entityType, String module) {
        try {
            String eventString = event.toString();
            AuditLog auditLog = AuditLog.builder()
                    .eventType(extractEventType(eventString)) // e.g., "TenderCreated", "EvaluationCompleted"
                    .entityType(entityType)
                    .entityId(extractEntityId(eventString))
                    .description(eventString)
                    .timestamp(LocalDateTime.now()) // Replace with event timestamp if available
                    .userId(extractUserId(eventString))
                    .module(module)
                    .build();
            auditLogRepository.save(auditLog);
            log.info("Audit log saved: {}", auditLog);
        } catch (Exception e) {
            log.error("Failed to process event: {}", event, e);
        }
    }

    // Placeholder extraction methods - refine with actual event structure
    private String extractEventType(String event) {
        return event.contains("eventType") ? event.split("eventType=")[1].split(",")[0] : "UNKNOWN";
    }

    private String extractEntityId(String event) {
        return event.contains("Id=") ? event.split("Id=")[1].split(",")[0] : "UNKNOWN";
    }

    private Long extractUserId(String event) {
        return event.contains("userId") ? Long.parseLong(event.split("userId=")[1].split(",")[0]) : null;
    }
}