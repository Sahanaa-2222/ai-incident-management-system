package com.finshield.incident_management.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class UpdateStatusRequest {
    @NotBlank(message = "Status is required")
    private String status;
}