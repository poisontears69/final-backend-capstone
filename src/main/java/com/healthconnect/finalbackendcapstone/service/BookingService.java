package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.dto.BookingDTO;
import com.healthconnect.finalbackendcapstone.model.Booking.AppointmentType;
import com.healthconnect.finalbackendcapstone.model.Booking.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDTO createBooking(BookingDTO bookingDTO);
    BookingDTO getBookingById(Long id);
    
    // Paginated methods
    Page<BookingDTO> getBookingsByUserId(Long userId, Pageable pageable);
    Page<BookingDTO> getBookingsByPatientId(Long patientId, Pageable pageable);
    Page<BookingDTO> getBookingsByDoctorId(Long doctorId, Pageable pageable);
    Page<BookingDTO> getBookingsByClinicId(Long clinicId, Pageable pageable);
    
    Page<BookingDTO> getBookingsByAppointmentType(AppointmentType appointmentType, Pageable pageable);
    Page<BookingDTO> getBookingsByStatus(BookingStatus status, Pageable pageable);
    Page<BookingDTO> getBookingsByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    Page<BookingDTO> getBookingsByDoctorIdAndStatus(Long doctorId, BookingStatus status, Pageable pageable);
    Page<BookingDTO> getBookingsByPatientIdAndStatus(Long patientId, BookingStatus status, Pageable pageable);
    Page<BookingDTO> getBookingsByUserIdAndStatus(Long userId, BookingStatus status, Pageable pageable);
    
    Page<BookingDTO> getBookingsByDoctorIdAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<BookingDTO> getBookingsByPatientIdAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    Page<BookingDTO> getBookingsByDoctorIdAndStatusAndDateRange(Long doctorId, BookingStatus status, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<BookingDTO> getBookingsByPatientIdAndStatusAndDateRange(Long patientId, BookingStatus status, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // Legacy non-paginated methods (for backward compatibility)
    List<BookingDTO> getBookingsByUserId(Long userId);
    List<BookingDTO> getBookingsByPatientId(Long patientId);
    List<BookingDTO> getBookingsByDoctorId(Long doctorId);
    List<BookingDTO> getBookingsByClinicId(Long clinicId);
    
    List<BookingDTO> getBookingsByAppointmentType(AppointmentType appointmentType);
    List<BookingDTO> getBookingsByStatus(BookingStatus status);
    List<BookingDTO> getBookingsByDateRange(LocalDateTime start, LocalDateTime end);
    
    List<BookingDTO> getBookingsByDoctorIdAndStatus(Long doctorId, BookingStatus status);
    List<BookingDTO> getBookingsByPatientIdAndStatus(Long patientId, BookingStatus status);
    List<BookingDTO> getBookingsByUserIdAndStatus(Long userId, BookingStatus status);
    
    List<BookingDTO> getBookingsByDoctorIdAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end);
    List<BookingDTO> getBookingsByPatientIdAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end);
    
    List<BookingDTO> getBookingsByDoctorIdAndStatusAndDateRange(Long doctorId, BookingStatus status, LocalDateTime start, LocalDateTime end);
    List<BookingDTO> getBookingsByPatientIdAndStatusAndDateRange(Long patientId, BookingStatus status, LocalDateTime start, LocalDateTime end);
    
    // Update and delete operations
    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);
    BookingDTO updateBookingStatus(Long id, BookingStatus status);
    void deleteBooking(Long id);
    
    // Dashboard/analytics methods
    long countBookingsByDoctorIdAndStatus(Long doctorId, BookingStatus status);
    long countBookingsByDoctorIdAndDateRange(Long doctorId, LocalDateTime start, LocalDateTime end);
} 