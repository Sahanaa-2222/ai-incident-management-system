package com.finshield.incident_management.repository;

import com.finshield.incident_management.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByIncidentIdOrderByTimestampDesc(Long incidentId);
}