package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.DoctorProfileRequest;
import com.healthconnect.finalbackendcapstone.dto.DoctorProfileResponse;
import com.healthconnect.finalbackendcapstone.exception.DuplicateResourceException;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.model.User;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import com.healthconnect.finalbackendcapstone.repository.UserRepository;
import com.healthconnect.finalbackendcapstone.util.PhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;
    private final PhoneNumberUtil phoneNumberUtil;

    /**
     * Create a new doctor profile
     * @param userId the user ID
     * @param request the doctor profile request
     * @return the created doctor profile
     */
    @Transactional
    public DoctorProfileResponse createDoctorProfile(Long userId, DoctorProfileRequest request) {
        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // Check if user has role DOCTOR
        if (user.getRole() != User.Role.DOCTOR) {
            throw new IllegalArgumentException("User must have DOCTOR role to create a doctor profile");
        }
        
        // Check if user already has a doctor profile
        if (doctorProfileRepository.findByUser(user).isPresent()) {
            throw new DuplicateResourceException("Doctor profile already exists for this user");
        }
        
        // Check if PRC number is already registered
        if (doctorProfileRepository.existsByPrcNumber(request.getPrcNumber())) {
            throw new DuplicateResourceException("PRC number already registered");
        }
        
        // Validate phone number if provided
        if (request.getPhoneNumber() != null) {
            String formattedPhoneNumber = phoneNumberUtil.formatToPhilippineNumber(request.getPhoneNumber());
            if (formattedPhoneNumber == null) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
            request.setPhoneNumber(formattedPhoneNumber);
        }
        
        // Create doctor profile
        DoctorProfile doctorProfile = new DoctorProfile();
        doctorProfile.setUser(user);
        updateDoctorProfileFromRequest(doctorProfile, request);
        
        // Save doctor profile
        DoctorProfile savedProfile = doctorProfileRepository.save(doctorProfile);
        
        // Return response
        return buildDoctorProfileResponse(savedProfile);
    }
    
    /**
     * Update an existing doctor profile
     * @param profileId the profile ID
     * @param request the doctor profile request
     * @return the updated doctor profile
     */
    @Transactional
    public DoctorProfileResponse updateDoctorProfile(Long profileId, DoctorProfileRequest request) {
        // Find existing doctor profile
        DoctorProfile doctorProfile = doctorProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found with id: " + profileId));
        
        // Check if PRC number is already registered (if changed)
        if (!doctorProfile.getPrcNumber().equals(request.getPrcNumber()) && 
            doctorProfileRepository.existsByPrcNumber(request.getPrcNumber())) {
            throw new DuplicateResourceException("PRC number already registered");
        }
        
        // Validate phone number if provided
        if (request.getPhoneNumber() != null) {
            String formattedPhoneNumber = phoneNumberUtil.formatToPhilippineNumber(request.getPhoneNumber());
            if (formattedPhoneNumber == null) {
                throw new IllegalArgumentException("Invalid phone number format");
            }
            request.setPhoneNumber(formattedPhoneNumber);
        }
        
        // Update doctor profile
        updateDoctorProfileFromRequest(doctorProfile, request);
        
        // Save doctor profile
        DoctorProfile savedProfile = doctorProfileRepository.save(doctorProfile);
        
        // Return response
        return buildDoctorProfileResponse(savedProfile);
    }
    
    /**
     * Get a doctor profile by ID
     * @param profileId the profile ID
     * @return the doctor profile
     */
    public DoctorProfileResponse getDoctorProfileById(Long profileId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found with id: " + profileId));
        
        return buildDoctorProfileResponse(doctorProfile);
    }
    
    /**
     * Get a doctor profile by user ID
     * @param userId the user ID
     * @return the doctor profile
     */
    public DoctorProfileResponse getDoctorProfileByUserId(Long userId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for user id: " + userId));
        
        return buildDoctorProfileResponse(doctorProfile);
    }
    
    /**
     * Search for doctors by name or specialization
     * @param searchTerm the search term
     * @param pageable pagination information
     * @return a page of doctor profiles
     */
    public Page<DoctorProfileResponse> searchDoctors(String searchTerm, Pageable pageable) {
        Page<DoctorProfile> doctorProfiles = doctorProfileRepository.searchDoctors(searchTerm, pageable);
        
        return doctorProfiles.map(this::buildDoctorProfileResponse);
    }
    
    /**
     * Get all verified doctors
     * @param pageable pagination information
     * @return a page of verified doctor profiles
     */
    public Page<DoctorProfileResponse> getVerifiedDoctors(Pageable pageable) {
        Page<DoctorProfile> doctorProfiles = doctorProfileRepository.findByIsVerifiedTrue(pageable);
        
        return doctorProfiles.map(this::buildDoctorProfileResponse);
    }
    
    /**
     * Get doctors by minimum years of experience
     * @param minYears minimum years of experience
     * @param pageable pagination information
     * @return a page of doctor profiles
     */
    public Page<DoctorProfileResponse> getDoctorsByExperience(int minYears, Pageable pageable) {
        // Calculate the year threshold (current year - minYears)
        int currentYear = LocalDate.now().getYear();
        int yearThreshold = currentYear - minYears;
        
        Page<DoctorProfile> doctorProfiles = doctorProfileRepository.findByExperienceGreaterThanEqual(yearThreshold, pageable);
        
        return doctorProfiles.map(this::buildDoctorProfileResponse);
    }
    
    /**
     * Verify a doctor profile (admin operation)
     * @param profileId the profile ID
     * @return the verified doctor profile
     */
    @Transactional
    public DoctorProfileResponse verifyDoctorProfile(Long profileId) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found with id: " + profileId));
        
        doctorProfile.setVerified(true);
        DoctorProfile savedProfile = doctorProfileRepository.save(doctorProfile);
        
        return buildDoctorProfileResponse(savedProfile);
    }
    
    /**
     * Delete a doctor profile
     * @param profileId the profile ID
     */
    @Transactional
    public void deleteDoctorProfile(Long profileId) {
        if (!doctorProfileRepository.existsById(profileId)) {
            throw new ResourceNotFoundException("Doctor profile not found with id: " + profileId);
        }
        
        doctorProfileRepository.deleteById(profileId);
    }
    
    /**
     * Helper method to update doctor profile from request
     * @param doctorProfile the doctor profile to update
     * @param request the request with new data
     */
    private void updateDoctorProfileFromRequest(DoctorProfile doctorProfile, DoctorProfileRequest request) {
        doctorProfile.setFirstName(request.getFirstName());
        doctorProfile.setMiddleName(request.getMiddleName());
        doctorProfile.setLastName(request.getLastName());
        doctorProfile.setSuffix(request.getSuffix());
        doctorProfile.setDateOfBirth(request.getDateOfBirth());
        doctorProfile.setGender(request.getGenderEnum());
        doctorProfile.setTitle(request.getTitle());
        doctorProfile.setDescription(request.getDescription());
        doctorProfile.setEmail(request.getEmail());
        doctorProfile.setPhoneNumber(request.getPhoneNumber());
        doctorProfile.setPrcNumber(request.getPrcNumber());
        doctorProfile.setS2Number(request.getS2Number());
        doctorProfile.setPtrNumber(request.getPtrNumber());
        doctorProfile.setPhicNumber(request.getPhicNumber());
        doctorProfile.setPracticingSince(request.getPracticingSince());
        doctorProfile.setProfilePictureUrl(request.getProfilePictureUrl());
    }
    
    /**
     * Helper method to build doctor profile response from entity
     * @param doctorProfile the doctor profile entity
     * @return the doctor profile response
     */
    private DoctorProfileResponse buildDoctorProfileResponse(DoctorProfile doctorProfile) {
        // Calculate years of experience if practicingSince is available
        Integer yearsOfExperience = null;
        if (doctorProfile.getPracticingSince() != null) {
            yearsOfExperience = LocalDate.now().getYear() - doctorProfile.getPracticingSince();
        }
        
        // Format phone number to local format for response
        String localPhoneNumber = null;
        if (doctorProfile.getPhoneNumber() != null) {
            localPhoneNumber = phoneNumberUtil.formatToLocalPhilippineNumber(doctorProfile.getPhoneNumber());
        }
        
        return DoctorProfileResponse.builder()
                .id(doctorProfile.getId())
                .userId(doctorProfile.getUser().getId())
                .firstName(doctorProfile.getFirstName())
                .middleName(doctorProfile.getMiddleName())
                .lastName(doctorProfile.getLastName())
                .suffix(doctorProfile.getSuffix())
                .fullName(doctorProfile.getFullName())
                .dateOfBirth(doctorProfile.getDateOfBirth())
                .gender(doctorProfile.getGender() != null ? doctorProfile.getGender().toString() : null)
                .title(doctorProfile.getTitle())
                .description(doctorProfile.getDescription())
                .email(doctorProfile.getEmail())
                .phoneNumber(localPhoneNumber)
                .prcNumber(doctorProfile.getPrcNumber())
                .s2Number(doctorProfile.getS2Number())
                .ptrNumber(doctorProfile.getPtrNumber())
                .phicNumber(doctorProfile.getPhicNumber())
                .practicingSince(doctorProfile.getPracticingSince())
                .yearsOfExperience(yearsOfExperience)
                .profilePictureUrl(doctorProfile.getProfilePictureUrl())
                .isVerified(doctorProfile.isVerified())
                .createdAt(doctorProfile.getCreatedAt())
                .updatedAt(doctorProfile.getUpdatedAt())
                .build();
    }
} 