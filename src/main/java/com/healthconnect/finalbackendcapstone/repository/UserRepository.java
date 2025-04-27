package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by email
     * @param email the email to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by phone number
     * @param phoneNumber the phone number to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    /**
     * Check if a user with the given email exists
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if a user with the given phone number exists
     * @param phoneNumber the phone number to check
     * @return true if a user with the phone number exists, false otherwise
     */
    boolean existsByPhoneNumber(String phoneNumber);
} 