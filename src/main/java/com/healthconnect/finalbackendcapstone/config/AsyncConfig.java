package com.healthconnect.finalbackendcapstone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {
    // Enables Spring's asynchronous method execution capability
    // and scheduled tasks for OTP cleanup
} 