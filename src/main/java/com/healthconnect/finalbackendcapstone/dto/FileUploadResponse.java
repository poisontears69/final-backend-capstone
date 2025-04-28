package com.healthconnect.finalbackendcapstone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponse {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String fileUrl;
} 