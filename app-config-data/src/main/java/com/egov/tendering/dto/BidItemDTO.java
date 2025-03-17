package com.egov.tendering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for bid item data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidItemDTO {
    private Long id;
    private Long tenderItemId;
    private Long criteriaId;
    private BigDecimal value;
    private String description;
}