package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "doctor_profiles", indexes = {
    @Index(name = "idx_doctor_prc", columnList = "prc_number", unique = true),
    @Index(name = "idx_doctor_name", columnList = "last_name, first_name"),
    @Index(name = "idx_doctor_verified", columnList = "is_verified"),
    @Index(name = "idx_doctor_practicing_since", columnList = "practicing_since"),
    @Index(name = "idx_doctor_email", columnList = "email"),
    @Index(name = "idx_doctor_phone", columnList = "phone_number")
})
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "prc_number", nullable = false, unique = true)
    private String prcNumber;

    @Column(name = "s2_number")
    private String s2Number;

    @Column(name = "ptr_number")
    private String ptrNumber;

    @Column(name = "phic_number")
    private String phicNumber;

    @Column(name = "practicing_since")
    private Integer practicingSince;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "is_verified", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Gender {
        MALE, FEMALE, OTHER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }

        public static Gender fromString(String value) {
            if (value == null) {
                return null;
            }
            try {
                return valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    // Helper method to get full name
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        fullName.append(firstName);
        
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(" ").append(middleName);
        }
        
        fullName.append(" ").append(lastName);
        
        if (suffix != null && !suffix.isEmpty()) {
            fullName.append(", ").append(suffix);
        }
        
        return fullName.toString();
    }
} 