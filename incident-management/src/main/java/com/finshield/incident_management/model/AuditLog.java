package com.finshield.incident_management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "incident_audit")
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long incidentId;
    private String action;
    private String performedBy;
    private String oldStatus;
    private String newStatus;
    private String details;
    
    @CreationTimestamp
    private Date timestamp;
}