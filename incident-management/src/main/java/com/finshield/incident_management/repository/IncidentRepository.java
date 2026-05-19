package com.finshield.incident_management.repository;

import com.finshield.incident_management.model.Incident;
import com.finshield.incident_management.model.Severity;
import com.finshield.incident_management.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(Status status);
    List<Incident> findBySeverity(Severity severity);
    
    @Query("SELECT COUNT(i) FROM Incident i WHERE i.severity = ?1")
    long countBySeverity(Severity severity);
    
    @Query("SELECT i.status, COUNT(i) FROM Incident i GROUP BY i.status")
    List<Object[]> getStatusSummary();
}