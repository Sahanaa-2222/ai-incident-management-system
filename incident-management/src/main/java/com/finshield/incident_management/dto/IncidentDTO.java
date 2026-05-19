package com.finshield.incident_management.dto;

import com.finshield.incident_management.model.Severity;
import com.finshield.incident_management.model.Status;
import lombok.Data;

@Data
public class IncidentDTO {
    private Long id;
    private String title;
    private String description;
    private Severity severity;
    private Status status;
    private int aiScore;
    private String createdBy;
    private String assignedTo;
    private String createdAt;
}