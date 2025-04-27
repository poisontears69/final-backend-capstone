package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "procedures")
public class MedicalProcedure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_record_id", nullable = false)
    private PatientRecord patientRecord;

    @Column(name = "procedure_name", nullable = false)
    private String procedureName;

    @Column(name = "procedure_date")
    private LocalDate procedureDate;

    @Column(name = "outcome", columnDefinition = "text")
    private String outcome;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
} 