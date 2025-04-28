package com.healthconnect.finalbackendcapstone.controller;

import com.healthconnect.finalbackendcapstone.dto.ApiResponse;
import com.healthconnect.finalbackendcapstone.dto.FileUploadResponse;
import com.healthconnect.finalbackendcapstone.service.CloudStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "${cors.allowed-origins}")
public class FileUploadController {

    private final CloudStorageService cloudStorageService;

    /**
     * Uploads a single file to Google Cloud Storage
     * 
     * @param file The file to upload
     * @param folder Optional folder path within the bucket
     * @return The upload response with the file URL
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileUploadResponse>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {
        
        try {
            // Validate the file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Please select a file to upload"));
            }
            
            // Upload to cloud storage
            String fileUrl = cloudStorageService.uploadFile(file, folder);
            
            // Create response
            FileUploadResponse response = FileUploadResponse.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .fileUrl(fileUrl)
                    .build();
            
            return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", response));
        } catch (IOException e) {
            log.error("Failed to upload file", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to upload file: " + e.getMessage()));
        }
    }
    
    /**
     * Uploads multiple files to Google Cloud Storage
     * 
     * @param files The files to upload
     * @param folder Optional folder path within the bucket
     * @return The upload response with the file URLs
     */
    @PostMapping(value = "/upload/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<List<FileUploadResponse>>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "folder", required = false) String folder) {
        
        try {
            // Validate files
            if (files.length == 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Please select at least one file to upload"));
            }
            
            List<FileUploadResponse> uploadResponses = new ArrayList<>();
            
            // Process each file
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileUrl = cloudStorageService.uploadFile(file, folder);
                    
                    FileUploadResponse response = FileUploadResponse.builder()
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .fileUrl(fileUrl)
                            .build();
                    
                    uploadResponses.add(response);
                }
            }
            
            return ResponseEntity.ok(ApiResponse.success("Files uploaded successfully", uploadResponses));
        } catch (IOException e) {
            log.error("Failed to upload files", e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to upload files: " + e.getMessage()));
        }
    }
    
    /**
     * Deletes a file from Google Cloud Storage
     * 
     * @param fileUrl The URL of the file to delete
     * @return Success or error response
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        boolean deleted = cloudStorageService.deleteFile(fileUrl);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success("File deleted successfully"));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete file"));
        }
    }
} 