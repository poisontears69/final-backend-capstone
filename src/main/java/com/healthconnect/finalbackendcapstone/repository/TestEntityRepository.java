package com.healthconnect.finalbackendcapstone.repository;

import com.healthconnect.finalbackendcapstone.model.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TestEntityRepository extends JpaRepository<TestEntity, Long> {
}
