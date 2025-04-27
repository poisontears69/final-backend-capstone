package com.healthconnect.finalbackendcapstone.util;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Utility class for working with environment variables
 */
@Component
public class EnvironmentUtil {

    private final Environment environment;

    public EnvironmentUtil(Environment environment) {
        this.environment = environment;
    }

    /**
     * Get an environment variable with a fallback value
     * @param key The environment variable key
     * @param defaultValue The default value if the environment variable is not set
     * @return The environment variable value or the default value
     */
    public String getProperty(String key, String defaultValue) {
        String value = environment.getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get an environment variable
     * @param key The environment variable key
     * @return The environment variable value or null if not set
     */
    public String getProperty(String key) {
        return environment.getProperty(key);
    }
} 