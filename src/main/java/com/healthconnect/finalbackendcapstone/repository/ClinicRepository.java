package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {

    List<Clinic> findByDoctorId(Long doctorId);

}
