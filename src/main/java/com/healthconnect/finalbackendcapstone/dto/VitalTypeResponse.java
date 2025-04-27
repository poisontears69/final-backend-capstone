package com.healthconnect.finalbackendcapstone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VitalTypeResponse {
    private Integer id;
    private String name;
    private String unit;
    private String description;
} 