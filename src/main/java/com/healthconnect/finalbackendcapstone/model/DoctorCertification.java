package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctor_certifications")
public class DoctorCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "certification_type", nullable = false)
    private CertificationType certificationType;

    @Column(name = "title", nullable = false)
    private String title;

    public enum CertificationType {
        LOCAL_BOARD("local_board"),
        INTERNATIONAL_BOARD("international_board");

        private final String value;

        CertificationType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
} 