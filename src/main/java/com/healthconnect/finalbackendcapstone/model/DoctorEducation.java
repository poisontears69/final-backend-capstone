package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Year;

@Data
@Entity
@Table(name = "doctor_education")
public class DoctorEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "education_type", nullable = false)
    private EducationType educationType;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Column(name = "year_completed")
    private Year yearCompleted;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    public enum EducationType {
        MEDICAL_SCHOOL("medical_school"),
        RESIDENCY("residency"),
        FELLOWSHIP("fellowship");

        private final String value;

        EducationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
} 