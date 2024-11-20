package com.simplogics.base.mapper;

import com.simplogics.base.dto.ReportCreateDto;
import com.simplogics.base.dto.ReportResponse;
import com.simplogics.base.entity.Image;
import com.simplogics.base.entity.Report;
import com.simplogics.base.entity.User;
import com.simplogics.base.projections.ReportResponseView;

import java.util.List;

public class ReportMapper {
    public static ReportResponse reportResponseDtoFromEntity(Report report){
        return ReportResponse.builder()
                .id(report.getId())
                .userId(report.getUser()!=null?report.getUser().getId():null)
                .description(report.getDescription())
                .dogSize(report.getDogSize())
                .agressionLevel(report.getAggression())
                .addressLocation(report.getLocation()!=null ? report.getLocation().getDescription(): null)
                .latitude(report.getLocation()!=null && report.getLocation().getLatitude()!=null ? String.valueOf(report.getLocation().getLatitude()):null)
                .longitude(report.getLocation()!=null && report.getLocation().getLongitude()!=null ? String.valueOf(report.getLocation().getLongitude()):null)
                .image(report.getImage()!=null?"data:image/jpeg;base64,"+report.getImage().getImageb64():null)
                .breed(report.getBreed())
                .dogStatus(report.getDogState())
                .createdAt(report.getCreatedAt()!=null?report.getCreatedAt().toString():null)
                .build();
    }
    public static List<ReportResponse> convertViewListToDto(List<ReportResponseView> reportReponseViews){
        return reportReponseViews.stream().map(ReportMapper::convertViewToDto).toList();
    }


    public static ReportResponse convertViewToDto(ReportResponseView reportReponseView){
        return ReportResponse.builder()
                .image(reportReponseView.getImage())
                .addressLocation(reportReponseView.getAddressLocation())
                .dogSize(reportReponseView.getDogSize())
                .build();
    }

    public static Report entityFromDto(ReportCreateDto reportCreateDto){
        return Report.builder()
                .breed(reportCreateDto.getBreed())
                .aggression(reportCreateDto.getAggression())
                .dogSize(reportCreateDto.getDogSize())
                .description(reportCreateDto.getDescription())
                .dogMentalState(reportCreateDto.getDogMentalState())
                .build();
    }
}
