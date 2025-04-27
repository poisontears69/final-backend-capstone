package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "family_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_record_id", nullable = false)
    private PatientRecord patientRecord;

    @Column(name = "health_condition", nullable = false)
    private String healthCondition;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "relative")
    private RelativeType relative;
    
    @Column(name = "age_at_diagnosis")
    private Integer ageAtDiagnosis;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum RelativeType {
        mother,
        father,
        sister,
        brother,
        maternal_grandmother,
        maternal_grandfather,
        paternal_grandmother,
        paternal_grandfather,
        other
    }
} 