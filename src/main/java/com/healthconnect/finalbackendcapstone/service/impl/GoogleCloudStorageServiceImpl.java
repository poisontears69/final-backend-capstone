package com.healthconnect.finalbackendcapstone.service.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.Storage.BlobTargetOption;
import com.google.cloud.storage.StorageException;
import com.healthconnect.finalbackendcapstone.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCloudStorageServiceImpl implements CloudStorageService {
    
    private final Storage storage;
    
    @Value("${gcp.storage.bucket-name}")
    private String bucketName;
    
    @Value("${gcp.storage.base-url}")
    private String baseUrl;
    
    @Override
    public String uploadFile(MultipartFile file, String folderName) throws IOException {
        try {
            // Get original file name and extract extension
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = getExtension(originalFilename);
            
            // Generate a unique file name to avoid collisions
            String fileName = generateUniqueFileName(fileExtension);
            
            // Add folder path if provided
            String objectName = folderName != null ? folderName + "/" + fileName : fileName;
            
            // Create blob info
            BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, objectName)
                    .setContentType(file.getContentType())
                    .build();
            
            // Upload the file
            Blob blob = storage.create(blobInfo, file.getBytes(), BlobTargetOption.predefinedAcl(Storage.PredefinedAcl.PUBLIC_READ));
            
            // Return the public URL
            return String.format("%s/%s/%s", baseUrl, bucketName, objectName);
        } catch (StorageException e) {
            log.error("Error uploading file to Google Cloud Storage", e);
            throw new IOException("Failed to upload file to Google Cloud Storage: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            // Extract the object name from the URL
            String objectName = fileUrl.substring(fileUrl.indexOf(bucketName) + bucketName.length() + 1);
            
            // Delete the blob
            BlobId blobId = BlobId.of(bucketName, objectName);
            boolean deleted = storage.delete(blobId);
            
            if (deleted) {
                log.info("File deleted successfully: {}", fileUrl);
            } else {
                log.warn("File not found or could not be deleted: {}", fileUrl);
            }
            
            return deleted;
        } catch (Exception e) {
            log.error("Error deleting file from Google Cloud Storage", e);
            return false;
        }
    }
    
    /**
     * Gets a signed URL for temporary access
     * 
     * @param objectName The object name in the bucket
     * @param expiryTimeInMinutes The expiry time in minutes
     * @return The signed URL
     */
    public URL getSignedUrl(String objectName, int expiryTimeInMinutes) {
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName, objectName)).build();
        return storage.signUrl(blobInfo, expiryTimeInMinutes, TimeUnit.MINUTES);
    }
    
    /**
     * Generates a unique file name using random alphanumeric strings
     * 
     * @param extension The file extension
     * @return A unique file name
     */
    private String generateUniqueFileName(String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomString = RandomStringUtils.randomAlphanumeric(8);
        return timestamp + "-" + randomString + "." + extension;
    }
    
    /**
     * Extracts the file extension from the original file name
     * 
     * @param fileName The original file name
     * @return The file extension
     */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
} 