package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "clinics")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;

    @Enumerated(EnumType.STRING)
    @Column(name = "consultation_mode", nullable = false)
    private ConsultationMode consultationMode = ConsultationMode.IN_CLINIC;

    @Column(name = "clinic_name", nullable = false)
    private String clinicName;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "region")
    private String region;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "description")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "landline_number")
    private String landlineNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "contact_person_email")
    private String contactPersonEmail;

    @Column(name = "contact_person_phone")
    private String contactPersonPhone;

    @Column(name = "consultation_fee", precision = 38, scale = 2)
    private BigDecimal consultationFee;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ConsultationMode {
        IN_CLINIC,
        ONLINE
    }

    @PrePersist
    @PreUpdate
    private void validateClinic() {
        if (consultationMode == ConsultationMode.IN_CLINIC) {
            if (addressLine1 == null || addressLine1.trim().isEmpty()) {
                throw new IllegalStateException("Address is required for in-clinic consultations");
            }
        } else {
            // For online clinics, clear physical location details
            addressLine1 = null;
            addressLine2 = null;
            city = null;
            province = null;
            region = null;
            zipCode = null;
            landmark = null;
            landlineNumber = null;
        }
    }
} 