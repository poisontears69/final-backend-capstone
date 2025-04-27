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
        
        // Check if schedule for this day already exists
        Optional<ClinicSchedule> existingSchedule = clinicScheduleRepository
                .findByClinicIdAndDayOfWeek(request.getClinicId(), request.getDayOfWeek());
        
        if (existingSchedule.isPresent()) {
            throw new IllegalStateException(
                    "Schedule already exists for clinic id: " + request.getClinicId() + 
                    " on " + request.getDayOfWeek());
        }
        
        // Validate time range
        if (request.getOpenTime().isAfter(request.getCloseTime())) {
            throw new IllegalArgumentException("Open time must be before close time");
        }
        
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
        
        // If day of week is changing, check for conflicts
        if (schedule.getDayOfWeek() != request.getDayOfWeek()) {
            Optional<ClinicSchedule> existingSchedule = clinicScheduleRepository
                    .findByClinicIdAndDayOfWeek(request.getClinicId(), request.getDayOfWeek());
            
            if (existingSchedule.isPresent() && !existingSchedule.get().getId().equals(id)) {
                throw new IllegalStateException(
                        "Schedule already exists for clinic id: " + request.getClinicId() + 
                        " on " + request.getDayOfWeek());
            }
        }
        
        // Validate time range
        if (request.getOpenTime().isAfter(request.getCloseTime())) {
            throw new IllegalArgumentException("Open time must be before close time");
        }
        
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
    
    private ClinicScheduleResponse mapToResponse(ClinicSchedule schedule) {
        return ClinicScheduleResponse.builder()
                .id(schedule.getId())
                .clinicId(schedule.getClinic().getId())
                .clinicName(schedule.getClinic().getClinicName())
                .dayOfWeek(schedule.getDayOfWeek())
                .openTime(schedule.getOpenTime())
                .closeTime(schedule.getCloseTime())
                .isActive(schedule.getIsActive())
                .clinicAddress(schedule.getClinic().getAddressLine1())
                .clinicCity(schedule.getClinic().getCity())
                .clinicProvince(schedule.getClinic().getProvince())
                .build();
    }
} 