package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.ClinicRequest;
import com.healthconnect.finalbackendcapstone.dto.ClinicResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.DoctorProfile;
import com.healthconnect.finalbackendcapstone.repository.ClinicRepository;
import com.healthconnect.finalbackendcapstone.repository.DoctorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final DoctorProfileRepository doctorProfileRepository;

    @Transactional
    public ClinicResponse createClinic(ClinicRequest request) {
        // Get doctor entity
        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        // For online clinics, check if doctor already has one
        if (request.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
            Optional<Clinic> existingOnlineClinic = clinicRepository.findByDoctorIdAndConsultationMode(
                request.getDoctorId(), Clinic.ConsultationMode.ONLINE);
            if (existingOnlineClinic.isPresent()) {
                throw new IllegalStateException("Doctor already has an online consultation clinic");
            }

            // For online clinics, set a default name if not provided
            if (request.getClinicName() == null || request.getClinicName().trim().isEmpty()) {
                request.setClinicName(doctor.getUser().getFullName() + "'s Online Consultation");
            }
        }

        // Create new clinic
        Clinic clinic = new Clinic();
        clinic.setDoctor(doctor);
        updateClinicFromRequest(clinic, request);

        Clinic savedClinic = clinicRepository.save(clinic);
        return mapToResponse(savedClinic);
    }

    @Transactional(readOnly = true)
    public ClinicResponse getClinicById(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + id));
        return mapToResponse(clinic);
    }

    @Transactional(readOnly = true)
    public ClinicResponse getDoctorClinic(Long doctorId, Long clinicId) {
        Clinic clinic = clinicRepository.findByDoctorIdAndId(doctorId, clinicId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Clinic not found with id: " + clinicId + " for doctor id: " + doctorId));
        return mapToResponse(clinic);
    }

    @Transactional(readOnly = true)
    public List<ClinicResponse> getClinicsByDoctorId(Long doctorId) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        List<Clinic> clinics = clinicRepository.findByDoctorId(doctorId);
        return clinics.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ClinicResponse> getClinicsByDoctorId(Long doctorId, Pageable pageable) {
        // Verify doctor exists
        if (!doctorProfileRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id: " + doctorId);
        }
        
        Page<Clinic> clinics = clinicRepository.findByDoctorId(doctorId, pageable);
        return clinics.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ClinicResponse> getClinicsByCity(String city, Pageable pageable) {
        // Only search physical clinics
        Page<Clinic> clinics = clinicRepository.findByCityAndConsultationMode(
            city, Clinic.ConsultationMode.IN_CLINIC, pageable);
        return clinics.map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ClinicResponse> searchClinicsByName(String name, Pageable pageable) {
        Page<Clinic> clinics = clinicRepository.findByClinicNameContainingIgnoreCase(name, pageable);
        return clinics.map(this::mapToResponse);
    }

    @Transactional
    public ClinicResponse updateClinic(Long id, ClinicRequest request) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + id));
        
        // Check if doctor ID is changing and verify new doctor exists
        if (!clinic.getDoctor().getId().equals(request.getDoctorId())) {
            DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));
            clinic.setDoctor(doctor);
        }

        // If changing to online mode, check if doctor already has an online clinic
        if (request.getConsultationMode() == Clinic.ConsultationMode.ONLINE && 
            clinic.getConsultationMode() != Clinic.ConsultationMode.ONLINE) {
            Optional<Clinic> existingOnlineClinic = clinicRepository.findByDoctorIdAndConsultationMode(
                request.getDoctorId(), Clinic.ConsultationMode.ONLINE);
            if (existingOnlineClinic.isPresent()) {
                throw new IllegalStateException("Doctor already has an online consultation clinic");
            }
        }
        
        updateClinicFromRequest(clinic, request);
        Clinic updatedClinic = clinicRepository.save(clinic);
        return mapToResponse(updatedClinic);
    }

    @Transactional
    public void deleteClinic(Long id) {
        if (!clinicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clinic not found with id: " + id);
        }
        clinicRepository.deleteById(id);
    }
    
    private void updateClinicFromRequest(Clinic clinic, ClinicRequest request) {
        clinic.setConsultationMode(request.getConsultationMode());
        clinic.setClinicName(request.getClinicName());

        // Only set physical clinic details for IN_CLINIC mode
        if (request.getConsultationMode() == Clinic.ConsultationMode.IN_CLINIC) {
            clinic.setAddressLine1(request.getAddressLine1());
            clinic.setAddressLine2(request.getAddressLine2());
            clinic.setCity(request.getCity());
            clinic.setProvince(request.getProvince());
            clinic.setRegion(request.getRegion());
            clinic.setZipCode(request.getZipCode());
            clinic.setLandmark(request.getLandmark());
            clinic.setLandlineNumber(request.getLandlineNumber());
        }

        // Common fields for both modes
        clinic.setDescription(request.getDescription());
        clinic.setEmail(request.getEmail());
        clinic.setPhoneNumber(request.getPhoneNumber());
        clinic.setContactPersonName(request.getContactPersonName());
        clinic.setContactPersonEmail(request.getContactPersonEmail());
        clinic.setContactPersonPhone(request.getContactPersonPhone());
        clinic.setConsultationFee(request.getConsultationFee());
    }
    
    private ClinicResponse mapToResponse(Clinic clinic) {
        return ClinicResponse.builder()
                .id(clinic.getId())
                .doctorId(clinic.getDoctor().getId())
                .doctorName(clinic.getDoctor().getUser().getFullName())
                .consultationMode(clinic.getConsultationMode())
                .clinicName(clinic.getClinicName())
                .addressLine1(clinic.getAddressLine1())
                .addressLine2(clinic.getAddressLine2())
                .city(clinic.getCity())
                .province(clinic.getProvince())
                .region(clinic.getRegion())
                .zipCode(clinic.getZipCode())
                .landmark(clinic.getLandmark())
                .description(clinic.getDescription())
                .email(clinic.getEmail())
                .landlineNumber(clinic.getLandlineNumber())
                .phoneNumber(clinic.getPhoneNumber())
                .contactPersonName(clinic.getContactPersonName())
                .contactPersonEmail(clinic.getContactPersonEmail())
                .contactPersonPhone(clinic.getContactPersonPhone())
                .consultationFee(clinic.getConsultationFee())
                .createdAt(clinic.getCreatedAt())
                .updatedAt(clinic.getUpdatedAt())
                .doctorSpecialty(clinic.getDoctor().getTitle())
                .doctorEmail(clinic.getDoctor().getEmail())
                .doctorPhoneNumber(clinic.getDoctor().getPhoneNumber())
                .build();
    }
} 