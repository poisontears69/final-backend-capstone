package com.healthconnect.finalbackendcapstone.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Builder
public class PatientResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    private Integer age;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String zipCode;
    private String fullAddress;
    private String profilePictureUrl;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    /**
     * Static method to calculate age from birthday
     * @param birthday the birthday
     * @return the age or null if birthday is null
     */
    public static Integer calculateAge(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }
        return Period.between(birthday, LocalDate.now()).getYears();
    }
} 