package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.healthconnect.finalbackendcapstone.exception.UserAlreadyExistsException;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String email, String phoneNumber, String password, User.Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists.");
        }

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException("Phone number already exists.");
        }
        // e.g. userRepository.findByEmail(email).isPresent()

        User user = new User();
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role);  // Use the Role enum
        user.setEmailVerified(false);
        user.setPhoneVerified(false);

        return userRepository.save(user);
    }
}

