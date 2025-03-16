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
public class EvaluationEvent {
    private Long eventId;
    private String eventType;
    private LocalDateTime timestamp;
    private Long evaluationId;
    private Long tenderId;
    private Long bidId;
    private Long evaluatedBy;
    private String evaluationResult;
    private Double score;
    private String comments;
    private Long awardedBy;
    private String awardComments;
    private String result;       // PASS, FAIL, CONDITIONAL
}