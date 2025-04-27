package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_bookings_datetime", columnList = "appointment_datetime"),
    @Index(name = "idx_bookings_status", columnList = "status"),
    @Index(name = "idx_bookings_type", columnList = "appointment_type"),
    @Index(name = "idx_bookings_doctor_status", columnList = "doctor_id, status"),
    @Index(name = "idx_bookings_doctor_datetime", columnList = "doctor_id, appointment_datetime"),
    @Index(name = "idx_bookings_patient_status", columnList = "patient_id, status"),
    @Index(name = "idx_bookings_patient_datetime", columnList = "patient_id, appointment_datetime")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatchSize(size = 20)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorProfile doctor;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id")
    private Clinic clinic;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false, length = 20)
    private AppointmentType appointmentType = AppointmentType.in_clinic;
    
    @Column(name = "appointment_datetime", nullable = false)
    private LocalDateTime appointmentDatetime;
    
    @Column(name = "reason", columnDefinition = "text")
    private String reason;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private BookingStatus status = BookingStatus.pending;
    
    @Column(name = "video_call_link", length = 512)
    private String videoCallLink;
    
    @Column(name = "booking_notes", columnDefinition = "text")
    private String bookingNotes;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum AppointmentType {
        in_clinic,
        online
    }
    
    public enum BookingStatus {
        pending,
        confirmed,
        cancelled,
        completed,
        no_show
    }
} 