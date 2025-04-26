package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {

    // Find all medications for a specific patient record
    List<Medication> findByPatientRecordId(Long patientRecordId);

    // Find medications within a date range
    @Query("SELECT m FROM Medication m WHERE " +
            "(m.startDate BETWEEN :startDate AND :endDate) OR " +
            "(m.endDate BETWEEN :startDate AND :endDate) OR " +
            "(m.startDate <= :startDate AND m.endDate >= :endDate)")
    List<Medication> findMedicationsByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Find medications by name (case-insensitive)
    List<Medication> findByMedicationNameContainingIgnoreCase(String medicationName);
}