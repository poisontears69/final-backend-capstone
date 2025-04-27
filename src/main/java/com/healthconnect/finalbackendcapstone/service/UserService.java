package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.SignupRequest;
import com.healthconnect.finalbackendcapstone.dto.SignupResponse;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import com.healthconnect.finalbackendcapstone.util.PasswordUtil;
import com.healthconnect.finalbackendcapstone.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Transactional
    public SignupResponse registerUser(SignupRequest signupRequest) {
        // Check if email already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
            throw new DuplicateResourceException("Phone number already in use");
        }

        // Create new user
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setPasswordHash(passwordUtil.encodePassword(signupRequest.getPassword()));

        // Set role (with validation)
        try {
            user.setRole(User.Role.valueOf(signupRequest.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            user.setRole(User.Role.PATIENT);
        }

        // Save user
        User savedUser = userRepository.save(user);

        // Build response
        return SignupResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .phoneNumber(savedUser.getPhoneNumber())
                .role(savedUser.getRole().name())
                .isEmailVerified(savedUser.isEmailVerified())
                .isPhoneVerified(savedUser.isPhoneVerified())
                .message("User registered successfully. Please verify your email.")
                .build();
    }
}
