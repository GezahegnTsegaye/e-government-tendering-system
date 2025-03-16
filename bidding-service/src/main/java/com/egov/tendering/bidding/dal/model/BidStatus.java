package com.egov.tendering.bidding.dal.model;

// Initial state when a bid is created but not yet submitted
// Bid has been submitted by the tenderer
// Bid is being evaluated by evaluators
// Bid has been accepted/awarded
// Bid has been rejected
public enum BidStatus {
    DRAFT,
    SUBMITTED,
    UNDER_EVALUATION,
    ACCEPTED,
    REJECTED,
    NOT_SUBMITTED,
    CANCELLED,
    EVALUATED,
    AWARDED,
    CONTRACTED,
    TERMINATED
}