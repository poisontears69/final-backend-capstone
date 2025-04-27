package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctor_affiliations")
public class DoctorAffiliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;
} 