package com.egov.tendering.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for bid data from the bidding service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidDTO {
    private Long id;
    private Long tenderId;
    private Long tendererId;
    private String tendererName;
    private LocalDateTime submissionTime;
    private String status;
    private List<BidItemDTO> items;
    private List<BidDocumentDTO> documents;
}