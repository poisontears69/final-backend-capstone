package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "substance_use")
public class SubstanceUse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_record_id", nullable = false)
    private PatientRecord patientRecord;

    @Column(name = "substance_name", nullable = false)
    private String substanceName;

    @Enumerated(EnumType.STRING)
    @Column(name = "usage_frequency")
    private UsageFrequency usageFrequency;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum UsageFrequency {
        DAILY("daily"),
        WEEKLY("weekly"),
        MONTHLY("monthly"),
        OCCASIONALLY("occasionally"),
        RARELY("rarely"),
        FORMER("former");

        private final String value;

        UsageFrequency(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
} 