package com.egov.tendering.audit.services.impl;


import com.egov.tendering.audit.dal.dto.*;
import com.egov.tendering.audit.dal.mapper.AuditMapper;
import com.egov.tendering.audit.dal.model.AuditActionType;
import com.egov.tendering.audit.dal.model.AuditLog;
import com.egov.tendering.audit.dal.repository.AuditLogRepository;

import com.egov.tendering.audit.specification.AuditLogSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing audit logs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditMapper auditMapper;

    /**
     * Create a new audit log entry
     */
    @Transactional
    public AuditLogResponse createAuditLog(AuditLogRequest request) {
        log.debug("Creating audit log entry for action: {}", request.getAction());

        AuditLog auditLog = auditMapper.toEntity(request);

        // Set hostname
        try {
            auditLog.setHostName(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            log.warn("Unable to determine hostname", e);
            auditLog.setHostName("unknown");
        }

        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return auditMapper.toResponse(savedAuditLog);
    }

    /**
     * Create an audit log entry from an event received from Kafka
     */
    @Transactional
    public AuditLogResponse createAuditLogFromEvent(AuditEventDTO event) {
        log.debug("Creating audit log entry from event for action: {}", event.getAction());

        AuditLog auditLog = auditMapper.toEntity(event);
        AuditLog savedAuditLog = auditLogRepository.save(auditLog);
        return auditMapper.toResponse(savedAuditLog);
    }

    /**
     * Get an audit log by ID
     */
    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLog(Long id) {
        log.debug("Getting audit log with ID: {}", id);

        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Audit log not found with ID: " + id));

        return auditMapper.toResponse(auditLog);
    }

    /**
     * Search audit logs with filtering
     */
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> searchAuditLogs(AuditLogFilter filter, Pageable pageable) {
        log.debug("Searching audit logs with filter: {}", filter);

        Specification<AuditLog> spec = buildSpecification(filter);
        Page<AuditLog> auditLogs = auditLogRepository.findAll(spec, pageable);

        return auditLogs.map(auditMapper::toResponse);
    }

    /**
     * Get audit logs by entity
     */
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogsByEntity(String entityType, String entityId, Pageable pageable) {
        log.debug("Getting audit logs for entity type: {}, ID: {}", entityType, entityId);

        Page<AuditLog> auditLogs = auditLogRepository.findByEntityTypeAndEntityIdOrderByTimestampDesc(
                entityType, entityId, pageable);

        return auditLogs.map(auditMapper::toResponse);
    }

    /**
     * Get audit logs by user
     */
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogsByUser(String userId, Pageable pageable) {
        log.debug("Getting audit logs for user: {}", userId);

        Page<AuditLog> auditLogs = auditLogRepository.findByUserIdOrderByTimestampDesc(userId, pageable);

        return auditLogs.map(auditMapper::toResponse);
    }

    /**
     * Get audit logs by action type
     */
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogsByActionType(AuditActionType actionType, Pageable pageable) {
        log.debug("Getting audit logs for action type: {}", actionType);

        Page<AuditLog> auditLogs = auditLogRepository.findByActionTypeOrderByTimestampDesc(actionType, pageable);

        return auditLogs.map(auditMapper::toResponse);
    }

    /**
     * Get audit logs by time range
     */
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> getAuditLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable) {
        log.debug("Getting audit logs between {} and {}", startTime, endTime);

        Page<AuditLog> auditLogs = auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(
                startTime, endTime, pageable);

        return auditLogs.map(auditMapper::toResponse);
    }

    /**
     * Get audit logs by correlation ID
     */
    @Transactional(readOnly = true)
    public List<AuditLogResponse> getAuditLogsByCorrelationId(String correlationId) {
        log.debug("Getting audit logs for correlation ID: {}", correlationId);

        List<AuditLog> auditLogs = auditLogRepository.findByCorrelationIdOrderByTimestampAsc(correlationId);

        return auditLogs.stream()
                .map(auditMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get audit statistics
     */
    @Transactional(readOnly = true)
    public AuditStatisticsDto getAuditStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting audit statistics between {} and {}", startTime, endTime);

        // Get total count
        long totalCount = auditLogRepository.count();

        // Get success and failure counts
        long successCount = 0;
        long failureCount = 0;
        Map<AuditActionType, Long> actionTypeCounts = new HashMap<>();
        Map<String, Long> entityTypeCounts = new HashMap<>();
        Map<String, Long> userActivityCounts = new HashMap<>();

        // This approach might not be efficient for large datasets
        // In a production environment, consider using aggregation queries
        AuditLogFilter filter = AuditLogFilter.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();

        Specification<AuditLog> spec = buildSpecification(filter);
        List<AuditLog> auditLogs = auditLogRepository.findAll(spec);

        for (AuditLog log : auditLogs) {
            if (log.isSuccess()) {
                successCount++;
            } else {
                failureCount++;
            }

            // Count by action type
            actionTypeCounts.put(log.getActionType(),
                    actionTypeCounts.getOrDefault(log.getActionType(), 0L) + 1);

            // Count by entity type
            entityTypeCounts.put(log.getEntityType(),
                    entityTypeCounts.getOrDefault(log.getEntityType(), 0L) + 1);

            // Count by user
            userActivityCounts.put(String.valueOf(log.getUserId()),
                    userActivityCounts.getOrDefault(log.getUserId(), 0L) + 1);
        }

        return AuditStatisticsDto.builder()
                .totalAuditLogs(totalCount)
                .successfulActions(successCount)
                .failedActions(failureCount)
                .actionTypeCounts(actionTypeCounts)
                .entityTypeCounts(entityTypeCounts)
                .userActivityCounts(userActivityCounts)
                .build();
    }

    /**
     * Get daily audit summary for a specific action type
     */
    @Transactional(readOnly = true)
    public List<AuditSummaryDto> getDailySummary(AuditActionType actionType, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Getting daily summary for action type: {} between {} and {}", actionType, startTime, endTime);

        List<Object[]> results = auditLogRepository.getActionSummaryByDay(actionType, startTime, endTime);

        List<AuditSummaryDto> summaries = new ArrayList<>();
        for (Object[] result : results) {
            LocalDate date = ((java.sql.Date) result[0]).toLocalDate();
            long count = ((Number) result[1]).longValue();

            // We would need additional queries to get success/failure counts
            // This is a simplified implementation
            AuditSummaryDto summary = AuditSummaryDto.builder()
                    .date(date)
                    .actionType(actionType.name())
                    .count(count)
                    .build();

            summaries.add(summary);
        }

        return summaries;
    }

    /**
     * Delete audit logs older than a certain date
     */
    @Transactional
    public long purgeOldAuditLogs(LocalDateTime olderThan) {
        log.info("Purging audit logs older than: {}", olderThan);

        AuditLogFilter filter = AuditLogFilter.builder()
                .endTime(olderThan)
                .build();

        Specification<AuditLog> spec = buildSpecification(filter);
        List<AuditLog> logsToDelete = auditLogRepository.findAll(spec);

        int count = logsToDelete.size();
        if (count > 0) {
            auditLogRepository.deleteAll(logsToDelete);
            log.info("Deleted {} audit logs", count);
        }

        return count;
    }

    /**
     * Build a specification for filtering audit logs
     */
    private Specification<AuditLog> buildSpecification(AuditLogFilter filter) {
        Specification<AuditLog> spec = Specification.where(null);

        if (filter.getUserId() != null && !filter.getUserId().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasUserId(filter.getUserId()));
        }

        if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasUsername(filter.getUsername()));
        }

        if (filter.getActionTypes() != null && !filter.getActionTypes().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasActionTypes(filter.getActionTypes()));
        }

        if (filter.getEntityType() != null && !filter.getEntityType().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasEntityType(filter.getEntityType()));
        }

        if (filter.getEntityId() != null && !filter.getEntityId().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasEntityId(filter.getEntityId()));
        }

        if (filter.getAction() != null && !filter.getAction().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasAction(filter.getAction()));
        }

        if (filter.getSuccess() != null) {
            spec = spec.and(AuditLogSpecification.hasSuccess(filter.getSuccess()));
        }

        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            spec = spec.and(AuditLogSpecification.containsKeyword(filter.getKeyword()));
        }

        if (filter.getCorrelationId() != null && !filter.getCorrelationId().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasCorrelationId(filter.getCorrelationId()));
        }

        if (filter.getServiceId() != null && !filter.getServiceId().isEmpty()) {
            spec = spec.and(AuditLogSpecification.hasServiceId(filter.getServiceId()));
        }

        if (filter.getStartTime() != null && filter.getEndTime() != null) {
            spec = spec.and(AuditLogSpecification.isInTimeRange(filter.getStartTime(), filter.getEndTime()));
        } else if (filter.getStartTime() != null) {
            spec = spec.and(AuditLogSpecification.isAfterTime(filter.getStartTime()));
        } else if (filter.getEndTime() != null) {
            spec = spec.and(AuditLogSpecification.isBeforeTime(filter.getEndTime()));
        }

        return spec;
    }
}
