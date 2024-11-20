package com.simplogics.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {
    private Long id;
    private Long userId;
    private String breed;
    private String description;
    private String dogSize;
    private String agressionLevel;
    private String image;
    private String latitude;
    private String longitude;
    private String addressLocation;
    private String dogStatus;
    private String createdAt;
}
