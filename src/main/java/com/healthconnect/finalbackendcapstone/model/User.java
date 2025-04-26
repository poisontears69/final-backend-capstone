package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users") // Ensure this matches your actual table name in MySQL
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING) // Store the Enum as a string
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "is_email_verified")
    private boolean isEmailVerified;

    @Column(name = "is_phone_verified")
    private boolean isPhoneVerified;

    // Enum for role
    public enum Role {
        patient,
        doctor,
        admin
    }
}


