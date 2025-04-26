package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.exception.UserAlreadyExistsException;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorProfileService {

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public DoctorProfile createDoctorProfile(Long userId, DoctorProfile profileData) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        if (doctorProfileRepository.existsByPrcNumber(profileData.getPrcNumber())) {
            throw new UserAlreadyExistsException("PRC Number already exists.");
        }

        profileData.setUser(userOpt.get());
        return doctorProfileRepository.save(profileData);
    }

    public Optional<DoctorProfile> getDoctorProfileByUserId(Long userId) {
        return doctorProfileRepository.findByUserId(userId);
    }
}
