package com.healthconnect.finalbackendcapstone.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Service interface for cloud storage operations
 */
public interface CloudStorageService {
    
    /**
     * Uploads a file to cloud storage and returns the public URL
     * 
     * @param file The file to upload
     * @param folderName The folder name (can be null)
     * @return The public URL of the uploaded file
     * @throws IOException If an error occurs during upload
     */
    String uploadFile(MultipartFile file, String folderName) throws IOException;
    
    /**
     * Deletes a file from cloud storage by its URL
     * 
     * @param fileUrl The URL of the file to delete
     * @return true if the file was deleted successfully, false otherwise
     */
    boolean deleteFile(String fileUrl);
} 