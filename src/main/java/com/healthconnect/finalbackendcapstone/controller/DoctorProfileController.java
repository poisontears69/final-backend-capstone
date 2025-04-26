package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.service.DoctorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/doctor-profiles")
public class DoctorProfileController {

    @Autowired
    private DoctorProfileService doctorProfileService;

    // Create a new doctor profile
    @PostMapping("/{userId}")
    public DoctorProfile createDoctorProfile(
            @PathVariable Long userId,
            @RequestBody DoctorProfile profileData
    ) {
        return doctorProfileService.createDoctorProfile(userId, profileData);
    }

    // Get a doctor profile by user ID
    @GetMapping("/{userId}")
    public Optional<DoctorProfile> getDoctorProfileByUserId(@PathVariable Long userId) {
        return doctorProfileService.getDoctorProfileByUserId(userId);
    }
}
