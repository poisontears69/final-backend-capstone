package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctor_social_links")
public class DoctorSocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "url", nullable = false)
    private String url;
} 