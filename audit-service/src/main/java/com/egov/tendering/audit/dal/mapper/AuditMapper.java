package com.egov.tendering.audit.dal.mapper;


import com.egov.tendering.audit.dal.dto.AuditEventDTO;
import com.egov.tendering.audit.dal.dto.AuditLogRequest;
import com.egov.tendering.audit.dal.dto.AuditLogResponse;
import com.egov.tendering.audit.dal.model.AuditLog;
import org.mapstruct.Mapper;

/**
 * Mapper for converting between AuditLog entities and DTOs
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    /**
     * Convert AuditLogRequest to AuditLog entity
     */
    AuditLog toEntity(AuditLogRequest request);

    /**
     * Convert AuditEventDto to AuditLog entity
     */
    AuditLog toEntity(AuditEventDTO event);

    /**
     * Convert AuditLog entity to AuditLogResponse
     */
    AuditLogResponse toResponse(AuditLog entity);
}
