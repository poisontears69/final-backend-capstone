package com.healthconnect.finalbackendcapstone.model;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private String id; // Firestore uses String IDs
    private String mobileNumber;
    private boolean verified;
    private Date createdAt;
    private Date updatedAt;

    // Required no-arg constructor for Firestore
    public User() {}
}