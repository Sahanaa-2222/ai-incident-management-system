package com.finshield.incident_management.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateIncidentRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
}