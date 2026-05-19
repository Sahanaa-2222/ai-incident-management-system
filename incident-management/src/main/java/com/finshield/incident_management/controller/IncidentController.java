package com.finshield.incident_management.controller;

import com.finshield.incident_management.dto.CreateIncidentRequest;
import com.finshield.incident_management.dto.IncidentDTO;
import com.finshield.incident_management.dto.UpdateStatusRequest;
import com.finshield.incident_management.model.Incident;
import com.finshield.incident_management.model.User;
import com.finshield.incident_management.service.IncidentService;
import com.finshield.incident_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    @Autowired
    private IncidentService incidentService;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create a new incident (AI scores it automatically)
    @PostMapping
    public ResponseEntity<IncidentDTO> createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        // For now, using default user (admin) - you can change this after adding authentication
        User defaultUser = userRepository.findById(1L).orElseThrow();
        
        Incident incident = incidentService.createIncident(
            request.getTitle(), 
            request.getDescription(), 
            defaultUser
        );
        
        return ResponseEntity.ok(convertToDTO(incident));
    }
    
    // Get all incidents
    @GetMapping
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        List<IncidentDTO> incidents = incidentService.getAllIncidents()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(incidents);
    }
    
    // Get incident by ID
    @GetMapping("/{id}")
    public ResponseEntity<IncidentDTO> getIncident(@PathVariable Long id) {
        Incident incident = incidentService.getIncident(id);
        return ResponseEntity.ok(convertToDTO(incident));
    }
    
    // Update incident status
    @PutMapping("/{id}/status")
    public ResponseEntity<IncidentDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        User defaultUser = userRepository.findById(1L).orElseThrow();
        Incident incident = incidentService.updateStatus(id, request.getStatus(), defaultUser);
        return ResponseEntity.ok(convertToDTO(incident));
    }
    
    // Delete incident
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }
    
    private IncidentDTO convertToDTO(Incident incident) {
        IncidentDTO dto = new IncidentDTO();
        dto.setId(incident.getId());
        dto.setTitle(incident.getTitle());
        dto.setDescription(incident.getDescription());
        dto.setSeverity(incident.getSeverity());
        dto.setStatus(incident.getStatus());
        dto.setAiScore(incident.getAiScore());
        dto.setCreatedBy(incident.getCreatedBy() != null ? incident.getCreatedBy().getUsername() : "System");
        dto.setCreatedAt(incident.getCreatedAt().toString());
        return dto;
    }
}