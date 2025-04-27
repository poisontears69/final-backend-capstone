package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Entity
@Table(name = "patients", indexes = {
    @Index(name = "idx_patients_name", columnList = "last_name, first_name"),
    @Index(name = "idx_patients_birthday", columnList = "birthday")
})
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper method to get full name
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        
        if (firstName != null && !firstName.isEmpty()) {
            fullName.append(firstName);
        }
        
        if (middleName != null && !middleName.isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(middleName);
        }
        
        if (lastName != null && !lastName.isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName);
        }
        
        return fullName.toString();
    }
    
    // Helper method to calculate patient's age based on birthday
    public Integer getAge() {
        if (birthday == null) {
            return null;
        }
        return Period.between(birthday, LocalDate.now()).getYears();
    }
    
    // Helper method to get complete address
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        
        if (addressLine1 != null && !addressLine1.isEmpty()) {
            address.append(addressLine1);
        }
        
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(addressLine2);
        }
        
        if (city != null && !city.isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(city);
        }
        
        if (province != null && !province.isEmpty()) {
            if (address.length() > 0) {
                address.append(", ");
            }
            address.append(province);
        }
        
        if (zipCode != null && !zipCode.isEmpty()) {
            if (address.length() > 0) {
                address.append(" ");
            }
            address.append(zipCode);
        }
        
        return address.toString();
    }
} 