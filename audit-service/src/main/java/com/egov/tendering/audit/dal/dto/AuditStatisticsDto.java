package com.egov.tendering.audit.dal.dto;

import com.egov.tendering.audit.dal.model.AuditActionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO for audit statistics by action type
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditStatisticsDto {

    private long totalAuditLogs;
    private long successfulActions;
    private long failedActions;
    private Map<AuditActionType, Long> actionTypeCounts;
    private Map<String, Long> entityTypeCounts;
    private Map<String, Long> userActivityCounts;
}
