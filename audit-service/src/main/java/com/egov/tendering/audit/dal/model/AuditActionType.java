package com.egov.tendering.audit.dal.model;

/**
 * Types of audit actions that can be logged
 */
public enum AuditActionType {
    // User-related actions
    USER_LOGIN,
    USER_LOGOUT,
    USER_CREATED,
    USER_UPDATED,
    USER_DELETED,
    USER_PASSWORD_CHANGED,
    USER_ROLE_CHANGED,

    // Tender-related actions
    TENDER_CREATED,
    TENDER_UPDATED,
    TENDER_PUBLISHED,
    TENDER_CLOSED,
    TENDER_CANCELLED,
    TENDER_DELETED,

    // Bid-related actions
    BID_SUBMITTED,
    BID_UPDATED,
    BID_WITHDRAWN,
    BID_EVALUATED,

    // Contract-related actions
    CONTRACT_CREATED,
    CONTRACT_UPDATED,
    CONTRACT_SIGNED,
    CONTRACT_COMPLETED,
    CONTRACT_TERMINATED,

    // Document-related actions
    DOCUMENT_UPLOADED,
    DOCUMENT_DOWNLOADED,
    DOCUMENT_DELETED,
    DOCUMENT_VERIFIED,

    // System-related actions
    SYSTEM_ERROR,
    SYSTEM_WARNING,
    SYSTEM_CONFIGURATION_CHANGED,

    // Security-related actions
    PERMISSION_GRANTED,
    PERMISSION_REVOKED,
    ACCESS_DENIED,

    // Generic actions
    DATA_VIEWED,
    DATA_CREATED,
    DATA_UPDATED,
    DATA_DELETED,
    CUSTOM
}