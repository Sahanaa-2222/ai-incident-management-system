package com.finshield.incident_management.service;

import com.finshield.incident_management.model.*;
import com.finshield.incident_management.repository.IncidentRepository;
import com.finshield.incident_management.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class IncidentService {
    
    @Autowired
    private IncidentRepository incidentRepository;
    
    @Autowired
    private AuditRepository auditRepository;
    
    @Autowired
    private AIScoringService aiScoringService;
    
    @Transactional
    public Incident createIncident(String title, String description, User user) {
        // Calculate AI score
        int score = aiScoringService.calculateScore(title, description);
        String severityLevel = aiScoringService.getSeverityFromScore(score);
        
        Incident incident = new Incident();
        incident.setTitle(title);
        incident.setDescription(description);
        incident.setAiScore(score);
        incident.setSeverity(Severity.valueOf(severityLevel));
        incident.setStatus(Status.OPEN);
        incident.setCreatedBy(user);
        incident.setCreatedAt(new Date());
        
        Incident saved = incidentRepository.save(incident);
        
        // Create audit log
        createAudit(saved.getId(), "CREATED", user.getUsername(), null, saved.getStatus().toString(), 
                    "AI Score: " + score + ", Severity: " + severityLevel);
        
        return saved;
    }
    
    @Transactional
    public Incident updateStatus(Long id, String newStatus, User user) {
        Incident incident = incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
        
        String oldStatus = incident.getStatus().toString();
        incident.setStatus(Status.valueOf(newStatus));
        incident.setUpdatedAt(new Date());
        
        if (newStatus.equals("RESOLVED")) {
            incident.setResolvedAt(new Date());
        }
        
        createAudit(id, "STATUS_CHANGE", user.getUsername(), oldStatus, newStatus, 
                   "Status updated by " + user.getUsername());
        
        return incidentRepository.save(incident);
    }
    
    private void createAudit(Long incidentId, String action, String performedBy, 
                            String oldStatus, String newStatus, String details) {
        AuditLog audit = new AuditLog();
        audit.setIncidentId(incidentId);
        audit.setAction(action);
        audit.setPerformedBy(performedBy);
        audit.setOldStatus(oldStatus);
        audit.setNewStatus(newStatus);
        audit.setDetails(details);
        audit.setTimestamp(new Date());
        auditRepository.save(audit);
    }
    
    public Incident getIncident(Long id) {
        return incidentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Incident not found"));
    }
    
    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }
    
    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }
}