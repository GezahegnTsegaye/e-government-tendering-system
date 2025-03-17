package com.egov.tendering.dto;

/**
 * Enum defining the allocation strategies for the tendering system.
 * Based on the contract selection types described in the research paper.
 */
public enum AllocationStrategy {
    /**
     * Single winner takes all.
     * The bidder with the highest overall score gets all items.
     * In case of a tie, a winner is randomly selected.
     */
    SINGLE,

    /**
     * Cooperative allocation.
     * For each item, the bidder with the highest score for that specific item is selected.
     * Items are allocated to different bidders based on their expertise.
     */
    COOPERATIVE,

    /**
     * Competitive allocation.
     * Multiple bidders with scores above a cut-off point are selected.
     * Items are allocated among them based on either average or proportional distribution.
     */
    COMPETITIVE
}