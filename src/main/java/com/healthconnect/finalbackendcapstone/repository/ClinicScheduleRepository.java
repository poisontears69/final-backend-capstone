package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import com.healthconnect.finalbackendcapstone.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface ClinicScheduleRepository extends JpaRepository<ClinicSchedule, Long> {
    List<ClinicSchedule> findByClinic(Clinic clinic);
    List<ClinicSchedule> findByClinicAndDayOfWeek(Clinic clinic, DayOfWeek dayOfWeek);

    @Query("SELECT cs FROM ClinicSchedule cs WHERE " +
            "cs.clinic = :clinic AND " +
            "cs.dayOfWeek = :dayOfWeek AND " +
            "((:openTime < cs.closeTime AND :closeTime > cs.openTime))")
    List<ClinicSchedule> findOverlappingSchedules(
            @Param("clinic") Clinic clinic,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("openTime") LocalTime openTime,
            @Param("closeTime") LocalTime closeTime);

    @Query("SELECT cs FROM ClinicSchedule cs WHERE " +
            "cs.clinic = :clinic AND " +
            "cs.dayOfWeek = :dayOfWeek AND " +
            "cs.id != :excludeId AND " +
            "((:openTime < cs.closeTime AND :closeTime > cs.openTime))")
    List<ClinicSchedule> findOverlappingSchedulesExcludingId(
            @Param("clinic") Clinic clinic,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("openTime") LocalTime openTime,
            @Param("closeTime") LocalTime closeTime,
            @Param("excludeId") Long excludeId);
}