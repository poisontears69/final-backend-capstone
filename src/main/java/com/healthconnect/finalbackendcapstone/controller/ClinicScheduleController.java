package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import com.healthconnect.finalbackendcapstone.service.ClinicScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.DayOfWeek;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ClinicScheduleController {

    private final ClinicScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ClinicSchedule> createSchedule(
            @RequestParam Long clinicId,
            @RequestBody ClinicSchedule schedule) {
        ClinicSchedule created = scheduleService.createSchedule(clinicId, schedule);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicSchedule> updateSchedule(
            @PathVariable Long id,
            @RequestBody ClinicSchedule schedule) {
        ClinicSchedule updated = scheduleService.updateSchedule(id, schedule);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicSchedule> getScheduleById(@PathVariable Long id) {
        ClinicSchedule schedule = scheduleService.getScheduleById(id);
        return schedule != null ? ResponseEntity.ok(schedule) : ResponseEntity.notFound().build();
    }

    @GetMapping("/clinic/{clinicId}")
    public ResponseEntity<List<ClinicSchedule>> getSchedulesByClinic(
            @PathVariable Long clinicId) {
        List<ClinicSchedule> schedules = scheduleService.getAllSchedulesByClinic(clinicId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/clinic/{clinicId}/day/{dayOfWeek}")
    public ResponseEntity<List<ClinicSchedule>> getSchedulesByClinicAndDay(
            @PathVariable Long clinicId,
            @PathVariable DayOfWeek dayOfWeek) {
        List<ClinicSchedule> schedules = scheduleService.getSchedulesByClinicAndDay(clinicId, dayOfWeek);
        return ResponseEntity.ok(schedules);
    }
}