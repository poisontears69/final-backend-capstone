package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Booking;
import com.healthconnect.finalbackendcapstone.model.Booking.AppointmentType;
import com.healthconnect.finalbackendcapstone.model.Booking.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Optional<Booking> findById(Long id);

    // Add pagination support for all queries that could return many results
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByUserId(Long userId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByPatientId(Long patientId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByDoctorId(Long doctorId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByClinicId(Long clinicId, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByAppointmentType(AppointmentType appointmentType, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByAppointmentDatetimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    // Optimized filtered queries with pagination
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByDoctorIdAndStatus(Long doctorId, BookingStatus status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByPatientIdAndStatus(Long patientId, BookingStatus status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByUserIdAndStatus(Long userId, BookingStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByDoctorIdAndAppointmentDatetimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    @EntityGraph(attributePaths = {"user", "patient", "doctor", "clinic"})
    Page<Booking> findByPatientIdAndAppointmentDatetimeBetween(Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Keep the specific list versions for cases where we need all results
    List<Booking> findByUserId(Long userId);
    List<Booking> findByPatientId(Long patientId);
    List<Booking> findByDoctorId(Long doctorId);
    List<Booking> findByClinicId(Long clinicId);
    List<Booking> findByAppointmentType(AppointmentType appointmentType);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByAppointmentDatetimeBetween(LocalDateTime start, LocalDateTime end);
    
    // Optimized complex queries
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.patient LEFT JOIN FETCH b.doctor LEFT JOIN FETCH b.clinic " +
           "WHERE b.doctor.id = :doctorId AND b.status = :status AND b.appointmentDatetime BETWEEN :start AND :end")
    Page<Booking> findByDoctorIdAndStatusAndDateRange(
            @Param("doctorId") Long doctorId, 
            @Param("status") BookingStatus status, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end,
            Pageable pageable);
    
    @Query("SELECT b FROM Booking b LEFT JOIN FETCH b.user LEFT JOIN FETCH b.patient LEFT JOIN FETCH b.doctor LEFT JOIN FETCH b.clinic " +
           "WHERE b.patient.id = :patientId AND b.status = :status AND b.appointmentDatetime BETWEEN :start AND :end")
    Page<Booking> findByPatientIdAndStatusAndDateRange(
            @Param("patientId") Long patientId, 
            @Param("status") BookingStatus status, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end,
            Pageable pageable);
            
    // Keep the list versions for backward compatibility
    @Query("SELECT b FROM Booking b WHERE b.doctor.id = :doctorId AND b.status = :status AND b.appointmentDatetime BETWEEN :start AND :end")
    List<Booking> findByDoctorIdAndStatusAndDateRange(
            @Param("doctorId") Long doctorId, 
            @Param("status") BookingStatus status, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end);
    
    @Query("SELECT b FROM Booking b WHERE b.patient.id = :patientId AND b.status = :status AND b.appointmentDatetime BETWEEN :start AND :end")
    List<Booking> findByPatientIdAndStatusAndDateRange(
            @Param("patientId") Long patientId, 
            @Param("status") BookingStatus status, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end);

    // Count methods for dashboard/analytics
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.doctor.id = :doctorId AND b.status = :status")
    long countByDoctorIdAndStatus(@Param("doctorId") Long doctorId, @Param("status") BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.doctor.id = :doctorId AND b.appointmentDatetime BETWEEN :start AND :end")
    long countByDoctorIdAndDateRange(@Param("doctorId") Long doctorId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
} 