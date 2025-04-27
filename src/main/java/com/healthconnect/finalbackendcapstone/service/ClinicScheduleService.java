package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.ClinicScheduleRequest;
import com.healthconnect.finalbackendcapstone.dto.ClinicScheduleResponse;
import com.healthconnect.finalbackendcapstone.exception.ResourceNotFoundException;
import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import com.healthconnect.finalbackendcapstone.repository.ClinicRepository;
import com.healthconnect.finalbackendcapstone.repository.ClinicScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClinicScheduleService {
    private final ClinicScheduleRepository clinicScheduleRepository;
    private final ClinicRepository clinicRepository;

    @Transactional
    public ClinicScheduleResponse createSchedule(ClinicScheduleRequest request) {
        // Validate clinic exists
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + request.getClinicId()));
        
        // Validate time range
        validateTimeRange(request.getOpenTime(), request.getCloseTime());
        
        // Check for overlapping schedules in the same clinic
        validateNoOverlappingSchedulesInClinic(request.getClinicId(), request.getDayOfWeek(), 
                request.getOpenTime(), request.getCloseTime(), null);
        
        // Check for overlapping schedules with other clinics of the same doctor
        validateNoOverlappingSchedulesWithOtherClinics(clinic.getDoctor().getId(), request.getClinicId(), 
                request.getDayOfWeek(), request.getOpenTime(), request.getCloseTime());
        
        // Create new schedule
        ClinicSchedule schedule = new ClinicSchedule();
        schedule.setClinic(clinic);
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setOpenTime(request.getOpenTime());
        schedule.setCloseTime(request.getCloseTime());
        schedule.setIsActive(request.getIsActive());
        
        ClinicSchedule savedSchedule = clinicScheduleRepository.save(schedule);
        return mapToResponse(savedSchedule);
    }
    
    @Transactional(readOnly = true)
    public ClinicScheduleResponse getScheduleById(Long id) {
        ClinicSchedule schedule = clinicScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic schedule not found with id: " + id));
        return mapToResponse(schedule);
    }
    
    @Transactional(readOnly = true)
    public List<ClinicScheduleResponse> getSchedulesByClinicId(Long clinicId) {
        // Verify clinic exists
        if (!clinicRepository.existsById(clinicId)) {
            throw new ResourceNotFoundException("Clinic not found with id: " + clinicId);
        }
        
        List<ClinicSchedule> schedules = clinicScheduleRepository.findByClinicId(clinicId);
        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ClinicScheduleResponse> getActiveSchedulesByClinicId(Long clinicId) {
        // Verify clinic exists
        if (!clinicRepository.existsById(clinicId)) {
            throw new ResourceNotFoundException("Clinic not found with id: " + clinicId);
        }
        
        List<ClinicSchedule> schedules = clinicScheduleRepository.findByClinicIdAndIsActive(clinicId, true);
        return schedules.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ClinicScheduleResponse getScheduleByClinicAndDay(Long clinicId, DayOfWeek dayOfWeek) {
        ClinicSchedule schedule = clinicScheduleRepository.findByClinicIdAndDayOfWeek(clinicId, dayOfWeek)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Clinic schedule not found for clinic id: " + clinicId + " on " + dayOfWeek));
        return mapToResponse(schedule);
    }
    
    @Transactional
    public ClinicScheduleResponse updateSchedule(Long id, ClinicScheduleRequest request) {
        ClinicSchedule schedule = clinicScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic schedule not found with id: " + id));
        
        // Check if the clinic is changing
        if (!schedule.getClinic().getId().equals(request.getClinicId())) {
            Clinic clinic = clinicRepository.findById(request.getClinicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + request.getClinicId()));
            schedule.setClinic(clinic);
        }
        
        // Validate time range
        validateTimeRange(request.getOpenTime(), request.getCloseTime());
        
        // Check for overlapping schedules in the same clinic
        validateNoOverlappingSchedulesInClinic(request.getClinicId(), request.getDayOfWeek(), 
                request.getOpenTime(), request.getCloseTime(), id);
        
        // Check for overlapping schedules with other clinics of the same doctor
        validateNoOverlappingSchedulesWithOtherClinics(schedule.getClinic().getDoctor().getId(), 
                request.getClinicId(), request.getDayOfWeek(), request.getOpenTime(), request.getCloseTime());
        
        // Update schedule
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setOpenTime(request.getOpenTime());
        schedule.setCloseTime(request.getCloseTime());
        schedule.setIsActive(request.getIsActive());
        
        ClinicSchedule updatedSchedule = clinicScheduleRepository.save(schedule);
        return mapToResponse(updatedSchedule);
    }
    
    @Transactional
    public void toggleScheduleActive(Long id, Boolean isActive) {
        ClinicSchedule schedule = clinicScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic schedule not found with id: " + id));
        
        schedule.setIsActive(isActive);
        clinicScheduleRepository.save(schedule);
    }
    
    @Transactional
    public void deleteSchedule(Long id) {
        if (!clinicScheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clinic schedule not found with id: " + id);
        }
        clinicScheduleRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteAllSchedulesForClinic(Long clinicId) {
        if (!clinicRepository.existsById(clinicId)) {
            throw new ResourceNotFoundException("Clinic not found with id: " + clinicId);
        }
        clinicScheduleRepository.deleteByClinicId(clinicId);
    }
    
    private void validateTimeRange(LocalTime openTime, LocalTime closeTime) {
        if (openTime.isAfter(closeTime)) {
            throw new IllegalArgumentException("Open time must be before close time");
        }
        
        // Removed business hours restriction to allow 24/7 operation
    }
    
    private void validateNoOverlappingSchedulesInClinic(Long clinicId, DayOfWeek dayOfWeek, 
            LocalTime openTime, LocalTime closeTime, Long excludeScheduleId) {
        // Get clinic to check consultation mode
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + clinicId));

        // For online clinics, we don't need to check for overlaps as they can handle parallel appointments
        if (clinic.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
            return;
        }

        // Get all schedules for this clinic on the same day
        List<ClinicSchedule> existingSchedules = clinicScheduleRepository.findByClinicId(clinicId)
                .stream()
                .filter(s -> s.getDayOfWeek() == dayOfWeek)
                .filter(s -> excludeScheduleId == null || !s.getId().equals(excludeScheduleId))
                .collect(Collectors.toList());
        
        // Check for overlaps with each existing schedule
        for (ClinicSchedule existingSchedule : existingSchedules) {
            boolean overlaps = !(closeTime.isBefore(existingSchedule.getOpenTime()) || 
                    openTime.isAfter(existingSchedule.getCloseTime()));
            
            if (overlaps) {
                throw new IllegalStateException(
                        "Schedule overlaps with existing schedule on " + dayOfWeek + 
                        " (" + existingSchedule.getOpenTime() + " - " + existingSchedule.getCloseTime() + ")");
            }
        }
    }
    
    private void validateNoOverlappingSchedulesWithOtherClinics(Long doctorId, Long currentClinicId, 
            DayOfWeek dayOfWeek, LocalTime openTime, LocalTime closeTime) {
        // Get current clinic to check consultation mode
        Clinic currentClinic = clinicRepository.findById(currentClinicId)
                .orElseThrow(() -> new ResourceNotFoundException("Clinic not found with id: " + currentClinicId));

        // For online clinics, we allow overlaps as doctors can handle multiple online consultations
        if (currentClinic.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
            return;
        }

        // Get all clinics for this doctor except the current one
        List<Clinic> otherClinics = clinicRepository.findByDoctorId(doctorId)
                .stream()
                .filter(c -> !c.getId().equals(currentClinicId))
                .collect(Collectors.toList());
        
        // For each clinic, check its schedules for the given day
        for (Clinic clinic : otherClinics) {
            // Skip overlap check with online clinics
            if (clinic.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
                continue;
            }

            List<ClinicSchedule> schedules = clinicScheduleRepository.findByClinicId(clinic.getId())
                    .stream()
                    .filter(s -> s.getDayOfWeek() == dayOfWeek)
                    .collect(Collectors.toList());
            
            for (ClinicSchedule schedule : schedules) {
                boolean overlaps = !(closeTime.isBefore(schedule.getOpenTime()) || 
                        openTime.isAfter(schedule.getCloseTime()));
                
                if (overlaps) {
                    throw new IllegalStateException(
                            "Schedule overlaps with existing schedule at clinic '" + 
                            clinic.getClinicName() + "' on " + dayOfWeek + 
                            " (" + schedule.getOpenTime() + " - " + schedule.getCloseTime() + ")");
                }
            }
        }
    }
    
    private ClinicScheduleResponse mapToResponse(ClinicSchedule schedule) {
        return ClinicScheduleResponse.builder()
                .id(schedule.getId())
                .clinicId(schedule.getClinic().getId())
                .clinicName(schedule.getClinic().getClinicName())
                .dayOfWeek(schedule.getDayOfWeek())
                .openTime(schedule.getOpenTime())
                .closeTime(schedule.getCloseTime())
                .consultationDurationMinutes(schedule.getConsultationDurationMinutes())
                .maxParallelAppointments(schedule.getMaxParallelAppointments())
                .isActive(schedule.getIsActive())
                .clinicAddress(schedule.getClinic().getAddressLine1())
                .clinicCity(schedule.getClinic().getCity())
                .clinicProvince(schedule.getClinic().getProvince())
                .consultationMode(schedule.getClinic().getConsultationMode())
                .build();
    }
} 