package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "allergies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_record_id", nullable = false)
    private PatientRecord patientRecord;

    @Column(name = "allergen", nullable = false)
    private String allergen;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private SeverityType severity;
    
    @Column(name = "reaction", columnDefinition = "text")
    private String reaction;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum SeverityType {
        mild,
        moderate,
        severe,
        @Column(name = "life-threatening")
        life_threatening
    }
} 