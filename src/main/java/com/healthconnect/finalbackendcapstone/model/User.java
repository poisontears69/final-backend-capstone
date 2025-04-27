package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_email", columnList = "email", unique = true),
    @Index(name = "idx_users_phone", columnList = "phone_number", unique = true),
    @Index(name = "idx_users_role", columnList = "role"),
    @Index(name = "idx_users_active", columnList = "is_active")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.PATIENT;

    @Column(name = "is_email_verified", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isEmailVerified = false;

    @Column(name = "is_phone_verified", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isPhoneVerified = false;

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Role {
        PATIENT, DOCTOR, ADMIN
    }

    // Helper method to get full name
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (firstName != null && !firstName.isEmpty()) {
            fullName.append(firstName);
        }
        
        if (lastName != null && !lastName.isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName);
        }
        
        // If no name is available, return email as identifier
        if (fullName.length() == 0 && email != null) {
            return email;
        }
        
        return fullName.toString();
    }
} 