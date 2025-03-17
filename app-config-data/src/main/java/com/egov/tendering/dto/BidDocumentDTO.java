package com.egov.tendering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for bid document data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidDocumentDTO {
    private Long id;
    private String name;
    private String fileType;
    private String fileUrl;
    private LocalDateTime uploadTime;
}