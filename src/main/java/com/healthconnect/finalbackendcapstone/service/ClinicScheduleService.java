package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.Clinic;
import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import com.healthconnect.finalbackendcapstone.repository.ClinicRepository;
import com.healthconnect.finalbackendcapstone.repository.ClinicScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClinicScheduleService {

    @Autowired
    private ClinicScheduleRepository scheduleRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    public ClinicSchedule createSchedule(Long clinicId, ClinicSchedule schedule) {
        Optional<Clinic> clinicOpt = clinicRepository.findById(clinicId);
        if (clinicOpt.isEmpty()) throw new RuntimeException("Clinic not found");

        schedule.setClinic(clinicOpt.get());
        return scheduleRepository.save(schedule);
    }

    public List<ClinicSchedule> getSchedulesByClinic(Long clinicId) {
        return scheduleRepository.findByClinicId(clinicId);
    }

    public ClinicSchedule updateSchedule(Long id, ClinicSchedule updated) {
        ClinicSchedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        existing.setDayOfWeek(updated.getDayOfWeek());
        existing.setOpenTime(updated.getOpenTime());
        existing.setCloseTime(updated.getCloseTime());
        existing.setIsActive(updated.getIsActive());

        return scheduleRepository.save(existing);
    }

    public void deleteSchedule(Long id) {
        scheduleRepository.deleteById(id);
    }
}
