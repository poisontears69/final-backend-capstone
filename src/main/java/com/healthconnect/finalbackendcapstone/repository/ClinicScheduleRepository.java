package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicScheduleRepository extends JpaRepository<ClinicSchedule, Long> {

    /**
     * Find all schedules for a specific clinic
     * @param clinic the clinic
     * @return list of clinic schedules
     */
    List<ClinicSchedule> findByClinic(Clinic clinic);
    
    /**
     * Find all schedules for a specific clinic ID
     * @param clinicId the clinic ID
     * @return list of clinic schedules
     */
    List<ClinicSchedule> findByClinicId(Long clinicId);
    
    /**
     * Find all active schedules for a specific clinic ID
     * @param clinicId the clinic ID
     * @param isActive active status
     * @return list of active clinic schedules
     */
    List<ClinicSchedule> findByClinicIdAndIsActive(Long clinicId, Boolean isActive);
    
    /**
     * Find schedule for a specific clinic and day of week
     * @param clinicId the clinic ID
     * @param dayOfWeek the day of week
     * @return the clinic schedule if found
     */
    Optional<ClinicSchedule> findByClinicIdAndDayOfWeek(Long clinicId, DayOfWeek dayOfWeek);
    
    /**
     * Find active schedule for a specific clinic and day of week
     * @param clinicId the clinic ID
     * @param dayOfWeek the day of week
     * @param isActive active status
     * @return the clinic schedule if found
     */
    Optional<ClinicSchedule> findByClinicIdAndDayOfWeekAndIsActive(Long clinicId, DayOfWeek dayOfWeek, Boolean isActive);
    
    /**
     * Delete all schedules for a specific clinic
     * @param clinicId the clinic ID
     */
    void deleteByClinicId(Long clinicId);
    
    /**
     * Count schedules for a specific clinic
     * @param clinicId the clinic ID
     * @return count of schedules
     */
    long countByClinicId(Long clinicId);
    
    /**
     * Count active schedules for a specific clinic
     * @param clinicId the clinic ID
     * @param isActive active status
     * @return count of active schedules
     */
    long countByClinicIdAndIsActive(Long clinicId, Boolean isActive);
} 