package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.model.TestEntity;
import com.healthconnect.finalbackendcapstone.service.TestEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestEntityController {

    @Autowired
    private TestEntityService testEntityService;

    // POST endpoint to save a TestEntity
    @PostMapping
    public TestEntity createTestEntity(@RequestBody TestEntity testEntity) {
        return testEntityService.saveTestEntity(testEntity);
    }

    // GET endpoint to retrieve a TestEntity by ID
    @GetMapping("/{id}")
    public Optional<TestEntity> getTestEntityById(@PathVariable Long id) {
        return testEntityService.getTestEntityById(id);
    }

    // Updated hello endpoint, no /api prefix needed here
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }


}
