package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // The user this patient is linked to

    private String firstName;
    private String middleName;
    private String lastName;
    private java.sql.Date birthday;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String province;
    private String zipCode;
    private String profilePictureUrl;
}
