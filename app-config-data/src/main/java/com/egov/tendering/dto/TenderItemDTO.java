package com.egov.tendering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for tender item data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenderItemDTO {
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private String unit;
    private Long criteriaId;
}