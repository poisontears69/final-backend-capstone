package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.exception.ScheduleConflictException;
import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.service.ClinicScheduleService;
import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import com.healthconnect.finalbackendcapstone.repository.ClinicRepository;
import com.healthconnect.finalbackendcapstone.repository.ClinicScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClinicScheduleServiceImpl implements ClinicScheduleService {

    private final ClinicScheduleRepository scheduleRepository;
    private final ClinicRepository clinicRepository;

    @Override
    public ClinicSchedule createSchedule(Long clinicId, ClinicSchedule schedule) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));

        schedule.setClinic(clinic);
        validateTimeRange(schedule.getOpenTime(), schedule.getCloseTime());
        checkForOverlaps(schedule, null);

        return scheduleRepository.save(schedule);
    }

    @Override
    public ClinicSchedule updateSchedule(Long id, ClinicSchedule scheduleUpdates) {
        validateTimeRange(scheduleUpdates.getOpenTime(), scheduleUpdates.getCloseTime());

        ClinicSchedule existingSchedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Create a temporary schedule for overlap checking
        ClinicSchedule tempSchedule = new ClinicSchedule();
        tempSchedule.setClinic(existingSchedule.getClinic()); // Use existing clinic
        tempSchedule.setDayOfWeek(scheduleUpdates.getDayOfWeek());
        tempSchedule.setOpenTime(scheduleUpdates.getOpenTime());
        tempSchedule.setCloseTime(scheduleUpdates.getCloseTime());

        checkForOverlaps(tempSchedule, id);

        // Update the existing schedule
        existingSchedule.setDayOfWeek(scheduleUpdates.getDayOfWeek());
        existingSchedule.setOpenTime(scheduleUpdates.getOpenTime());
        existingSchedule.setCloseTime(scheduleUpdates.getCloseTime());
        existingSchedule.setIsActive(scheduleUpdates.getIsActive());

        return scheduleRepository.save(existingSchedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public ClinicSchedule getScheduleById(Long id) {
        return scheduleRepository.findById(id).orElse(null);
    }

    @Override
    public List<ClinicSchedule> getAllSchedulesByClinic(Long clinicId) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));
        return scheduleRepository.findByClinic(clinic);
    }

    @Override
    public List<ClinicSchedule> getSchedulesByClinicAndDay(Long clinicId, DayOfWeek dayOfWeek) {
        Clinic clinic = clinicRepository.findById(clinicId)
                .orElseThrow(() -> new RuntimeException("Clinic not found"));
        return scheduleRepository.findByClinicAndDayOfWeek(clinic, dayOfWeek);
    }

    private void validateTimeRange(LocalTime openTime, LocalTime closeTime) {
        if (closeTime.isBefore(openTime)) {
            throw new IllegalArgumentException("Close time must be after open time");
        }
        if (closeTime.equals(openTime)) {
            throw new IllegalArgumentException("Close time cannot be equal to open time");
        }
    }

    private void checkForOverlaps(ClinicSchedule schedule, Long excludeId) {
        List<ClinicSchedule> overlappingSchedules;

        if (excludeId == null) {
            overlappingSchedules = scheduleRepository.findOverlappingSchedules(
                    schedule.getClinic(),
                    schedule.getDayOfWeek(),
                    schedule.getOpenTime(),
                    schedule.getCloseTime());
        } else {
            overlappingSchedules = scheduleRepository.findOverlappingSchedulesExcludingId(
                    schedule.getClinic(),
                    schedule.getDayOfWeek(),
                    schedule.getOpenTime(),
                    schedule.getCloseTime(),
                    excludeId);
        }

        if (!overlappingSchedules.isEmpty()) {
            ClinicSchedule conflict = overlappingSchedules.get(0);
            throw new ScheduleConflictException(
                    String.format("Schedule conflicts with existing schedule (ID: %d) %s-%s",
                            conflict.getId(),
                            conflict.getOpenTime(),
                            conflict.getCloseTime())
            );
        }
    }
}