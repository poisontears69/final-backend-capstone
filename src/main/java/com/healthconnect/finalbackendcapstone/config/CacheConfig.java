package com.healthconnect.finalbackendcapstone.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        
        // Define cache names for service-level caching
        cacheManager.setCacheNames(Arrays.asList(
            "doctors", 
            "patients", 
            "users", 
            "clinics", 
            "patientRecords",
            "medications",
            "immunizations"
        ));
        
        return cacheManager;
    }
} 