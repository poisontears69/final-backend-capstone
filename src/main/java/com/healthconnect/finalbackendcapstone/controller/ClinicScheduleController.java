package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import com.healthconnect.finalbackendcapstone.service.ClinicScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinics/{clinicId}/schedules")
public class ClinicScheduleController {

    @Autowired
    private ClinicScheduleService scheduleService;

    @PostMapping
    public ClinicSchedule create(@PathVariable Long clinicId, @RequestBody ClinicSchedule schedule) {
        return scheduleService.createSchedule(clinicId, schedule);
    }

    @GetMapping
    public List<ClinicSchedule> getAll(@PathVariable Long clinicId) {
        return scheduleService.getSchedulesByClinic(clinicId);
    }

    @PutMapping("/{scheduleId}")
    public ClinicSchedule update(@PathVariable Long scheduleId, @RequestBody ClinicSchedule schedule) {
        return scheduleService.updateSchedule(scheduleId, schedule);
    }

    @DeleteMapping("/{scheduleId}")
    public void delete(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
    }
}
