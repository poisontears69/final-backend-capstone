package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.ClinicSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClinicScheduleRepository extends JpaRepository<ClinicSchedule, Long> {
    List<ClinicSchedule> findByClinicId(Long clinicId);
}
