package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.LoginRequest;
import com.healthconnect.finalbackendcapstone.dto.LoginResponse;
import com.healthconnect.finalbackendcapstone.dto.SignupRequest;
import com.healthconnect.finalbackendcapstone.dto.SignupResponse;
import com.healthconnect.finalbackendcapstone.exception.DuplicateResourceException;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import com.healthconnect.finalbackendcapstone.security.JwtUtil;
import com.healthconnect.finalbackendcapstone.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    /**
     * Register a new user
     * @param signupRequest the signup request containing user details
     * @return a response containing the created user details
     * @throws DuplicateResourceException if email or phone number already exists
     */
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
            user.setRole(User.Role.PATIENT); // Default to PATIENT if invalid role
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
                .isActive(savedUser.isActive())
                .message("User registered successfully. Please verify your email and phone.")
                .build();
    }
    
    /**
     * Authenticate a user and generate JWT token
     * @param loginRequest the login request containing credentials
     * @return a response containing the user details and JWT token
     * @throws UsernameNotFoundException if user not found
     * @throws BadCredentialsException if password is incorrect
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {
        // Find user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginRequest.getEmail()));
        
        // Check if user is active
        if (!user.isActive()) {
            throw new BadCredentialsException("Account is disabled");
        }
        
        // Verify password
        if (!passwordUtil.verifyPassword(loginRequest.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid password");
        }
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user);
        
        // Build response
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .isEmailVerified(user.isEmailVerified())
                .isPhoneVerified(user.isPhoneVerified())
                .isActive(user.isActive())
                .token(token)
                .build();
    }
    
    /**
     * Get user by ID
     * @param id the user ID
     * @return the user or null if not found
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    /**
     * Get user by email
     * @param email the user email
     * @return the user or null if not found
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
} 