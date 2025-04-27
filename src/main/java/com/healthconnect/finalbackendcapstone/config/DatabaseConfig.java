package com.healthconnect.finalbackendcapstone.config;

import com.healthconnect.finalbackendcapstone.util.EnvironmentUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Database configuration class that can use environment variables
 * Note: This is just an example and you can continue using application.yml for simpler configurations
 */
@Configuration
public class DatabaseConfig {

    private final EnvironmentUtil environmentUtil;

    public DatabaseConfig(EnvironmentUtil environmentUtil) {
        this.environmentUtil = environmentUtil;
    }

    /**
     * Programmatic datasource configuration using environment variables
     * This is just an example and won't be used if Spring Boot properties work as expected
     */
    @Bean
    @Profile("manual-config") // Only activate with this profile
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        // Get database configuration from environment variables with defaults from application.yml
        String url = environmentUtil.getProperty("DB_URL", 
                "jdbc:mysql://mainline.proxy.rlwy.net:28437/railway");
        String username = environmentUtil.getProperty("DB_USERNAME", "root");
        String password = environmentUtil.getProperty("DB_PASSWORD", 
                "zbPJGPMNyPvurRvJZqLBFZpxMYYwDyzd");

        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        
        return dataSource;
    }
} 