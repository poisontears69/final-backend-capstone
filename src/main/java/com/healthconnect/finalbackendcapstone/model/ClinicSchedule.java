package com.healthconnect.finalbackendcapstone.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "clinic_schedules")
public class ClinicSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "consultation_duration_minutes", nullable = false)
    private Integer consultationDurationMinutes = 30; // Default 30-minute slots

    @Column(name = "max_parallel_appointments")
    private Integer maxParallelAppointments = 1; // Default 1 for in-clinic, can be more for online

    @Column(name = "is_active", columnDefinition = "tinyint(1) default 1")
    private Boolean isActive = true;

    @PrePersist
    @PreUpdate
    private void validateSchedule() {
        // If this is an online clinic schedule, allow parallel appointments
        if (clinic.getConsultationMode() == Clinic.ConsultationMode.ONLINE) {
            if (maxParallelAppointments == null || maxParallelAppointments < 1) {
                maxParallelAppointments = 3; // Default to 3 parallel online appointments
            }
        } else {
            maxParallelAppointments = 1; // Physical clinics can only handle one appointment at a time
        }

        // Ensure consultation duration is set and valid
        if (consultationDurationMinutes == null || consultationDurationMinutes < 15) {
            consultationDurationMinutes = 30; // Default to 30 minutes if not set or too short
        }
    }
} 