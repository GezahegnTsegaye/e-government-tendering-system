package com.egov.tendering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for tender criteria data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenderCriteriaDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal weight;
    private String criteriaType;
    private String evaluationMethod;
}