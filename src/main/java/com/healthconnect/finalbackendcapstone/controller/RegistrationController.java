package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public User registerUser(@RequestBody RegistrationRequest request) {
        return registrationService.registerUser(
                request.getEmail(),
                request.getPhoneNumber(),
                request.getPassword(),
                request.getRole()
        );
    }

    // Inner static class to receive the registration data
    public static class RegistrationRequest {
        private String email;
        private String phoneNumber;
        private String password;
        private User.Role role;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
    }
}
