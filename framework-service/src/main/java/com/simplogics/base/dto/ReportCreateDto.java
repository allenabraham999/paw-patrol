package com.simplogics.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportCreateDto {
    private String breed;
    private String description;
    private String dogSize;
    private String dogMentalState;
    private String aggression;
    private String image;
    private String latitude;
    private String longitude;
}
