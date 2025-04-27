package com.healthconnect.finalbackendcapstone.config;

import com.healthconnect.finalbackendcapstone.util.EnvironmentUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * CORS configuration that uses environment variables
 */
@Configuration
public class CorsConfig {

    private final EnvironmentUtil environmentUtil;

    public CorsConfig(EnvironmentUtil environmentUtil) {
        this.environmentUtil = environmentUtil;
    }

    @Bean
    public CorsFilter corsFilter() {
        // Get allowed origins from environment variable or use default
        String allowedOriginsStr = environmentUtil.getProperty("CORS_ALLOWED_ORIGINS", "http://localhost:4200");
        String[] allowedOrigins = allowedOriginsStr.split(",");

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                "Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList(
                "Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}