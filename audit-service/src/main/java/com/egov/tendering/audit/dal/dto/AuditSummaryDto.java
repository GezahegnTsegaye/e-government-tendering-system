package com.egov.tendering.audit.dal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for audit summary statistics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditSummaryDto {

    private LocalDate date;
    private String actionType;
    private long count;
    private long successCount;
    private long failureCount;
}

