package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import java.time.DayOfWeek;
import java.util.List;

public interface ClinicScheduleService {
    ClinicSchedule createSchedule(Long clinicId, ClinicSchedule schedule);
    ClinicSchedule updateSchedule(Long id, ClinicSchedule schedule);
    void deleteSchedule(Long id);
    ClinicSchedule getScheduleById(Long id);
    List<ClinicSchedule> getAllSchedulesByClinic(Long clinicId);
    List<ClinicSchedule> getSchedulesByClinicAndDay(Long clinicId, DayOfWeek dayOfWeek);
}