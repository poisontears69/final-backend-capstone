package com.healthconnect.finalbackendcapstone.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${gcp.storage.bucket-name}")
    private String bucketName;

    @Value("${gcp.storage.credentials-file-path:classpath:gcp-credentials.json}")
    private String credentialsFilePath;

    /**
     * Creates a Google Cloud Storage client bean
     * @return Storage client bean
     * @throws IOException if the credentials file cannot be read
     */
    @Bean
    public Storage storage() throws IOException {
        InputStream credentialsStream;
        
        if (credentialsFilePath.startsWith("classpath:")) {
            String path = credentialsFilePath.substring("classpath:".length());
            credentialsStream = new ClassPathResource(path).getInputStream();
        } else {
            // For file system paths
            credentialsStream = new java.io.FileInputStream(credentialsFilePath);
        }
        
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
    
    /**
     * @return The configured bucket name
     */
    @Bean
    public String bucketName() {
        return bucketName;
    }
} 