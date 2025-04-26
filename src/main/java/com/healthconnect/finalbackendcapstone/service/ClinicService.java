package com.healthconnect.finalbackendcapstone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.repository.ClinicRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;

import java.util.List;

@Service
public class ClinicService {

    @Autowired
    private ClinicRepository clinicRepo;

    @Autowired
    private DoctorProfileRepository doctorProfileRepo;

    // Create Clinic
    public Clinic createClinic(Long doctorId, Clinic clinic) {
        DoctorProfile doctor = doctorProfileRepo.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));

        clinic.setDoctor(doctor);
        return clinicRepo.save(clinic);
    }

    // Get Clinics by Doctor ID
    public List<Clinic> getClinicsByDoctor(Long doctorId) {
        return clinicRepo.findByDoctorId(doctorId);
    }

    // Delete Clinic by ID
    public void deleteClinic(Long clinicId) {
        clinicRepo.deleteById(clinicId);
    }

    // Update Clinic by ID
    public Clinic updateClinic(Long id, Clinic clinic) {
        Clinic existingClinic = clinicRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clinic not found"));

        existingClinic.setClinicName(clinic.getClinicName());
        existingClinic.setAddressLine1(clinic.getAddressLine1());
        existingClinic.setAddressLine2(clinic.getAddressLine2());
        existingClinic.setCity(clinic.getCity());
        existingClinic.setProvince(clinic.getProvince());
        existingClinic.setRegion(clinic.getRegion());
        existingClinic.setZipCode(clinic.getZipCode());
        existingClinic.setLandmark(clinic.getLandmark());
        existingClinic.setDescription(clinic.getDescription());
        existingClinic.setEmail(clinic.getEmail());
        existingClinic.setLandlineNumber(clinic.getLandlineNumber());
        existingClinic.setPhoneNumber(clinic.getPhoneNumber());
        existingClinic.setContactPersonName(clinic.getContactPersonName());
        existingClinic.setContactPersonEmail(clinic.getContactPersonEmail());
        existingClinic.setContactPersonPhone(clinic.getContactPersonPhone());
        existingClinic.setConsultationFee(clinic.getConsultationFee());

        return clinicRepo.save(existingClinic);
    }
}
