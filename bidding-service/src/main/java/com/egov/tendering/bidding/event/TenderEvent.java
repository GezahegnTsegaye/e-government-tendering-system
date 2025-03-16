package com.egov.tendering.bidding.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenderEvent {
    private Long eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private Long tenderId;
    private String tenderReference;
    private String tenderTitle;
    private String tenderStatus;
    private LocalDateTime publicationDate;
    private LocalDateTime submissionDeadline;
    private LocalDateTime previousDeadline;
    private String cancelReason;
}