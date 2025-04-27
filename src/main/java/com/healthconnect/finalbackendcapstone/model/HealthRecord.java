package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_record_id", nullable = false)
    private PatientRecord patientRecord;

    @Column(name = "type", nullable = false)
    private String type;
    
    @Column(name = "result_date", nullable = false)
    private LocalDate resultDate;
    
    @Column(name = "lab_name")
    private String labName;
    
    @Column(name = "remarks", columnDefinition = "text")
    private String remarks;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
} 