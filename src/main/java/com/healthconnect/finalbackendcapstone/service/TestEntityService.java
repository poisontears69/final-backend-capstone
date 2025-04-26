package com.healthconnect.finalbackendcapstone.service;

import com.healthconnect.finalbackendcapstone.model.TestEntity;
import com.healthconnect.finalbackendcapstone.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestEntityService {
    @Autowired
    private TestEntityRepository testEntityRepository;

    // Save a TestEntity
    public TestEntity saveTestEntity(TestEntity testEntity) {
        return testEntityRepository.save(testEntity);
    }

    // Retrieve a TestEntity by ID
    public Optional<TestEntity> getTestEntityById(Long id) {
        return testEntityRepository.findById(id);
    }
}
