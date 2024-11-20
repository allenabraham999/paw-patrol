package com.simplogics.base.service;

import com.simplogics.base.dto.DangerNotify;
import com.simplogics.base.dto.ReportCreateDto;
import com.simplogics.base.dto.ReportResponse;
import com.simplogics.base.entity.Image;
import com.simplogics.base.entity.Location;
import com.simplogics.base.entity.Report;
import com.simplogics.base.entity.User;
import com.simplogics.base.enums.DogAttendedState;
import com.simplogics.base.enums.RiskLevels;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.mapper.ReportMapper;
import com.simplogics.base.projections.ReportResponseView;
import com.simplogics.base.repository.ReportRepository;
import com.simplogics.base.specifications.ReportSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

@Service
public class ReportService implements IReportService{
    //TODO: save date and time
    @Autowired
    ReportRepository reportRepository;
    @Override
    public ReportResponse createReport(User user, ReportCreateDto reportCreateDto){
        Report report = ReportMapper.entityFromDto(reportCreateDto);
        Image image = Image.builder()
                .imageb64(reportCreateDto.getImage())
                .build();
        Location location = Location.builder()
                .description(reportCreateDto.getDescription())
                .latitude(Float.valueOf(reportCreateDto.getLatitude()))
                .longitude(Float.valueOf(reportCreateDto.getLongitude()))
                .build();
        report.setImage(image);
        report.setUser(user);
        report.setLocation(location);
        report.setCreatedAt(Instant.now());
        return ReportMapper.reportResponseDtoFromEntity(reportRepository.save(report));
    }

    @Override
    public List<ReportResponse> getAllReports(User user, String latitude, String longitude){
        List<ReportResponseView> reportResponses = reportRepository.getReportsFilteredByLocation(user.getId(), Float.valueOf(latitude), Float.valueOf(longitude));
        List<ReportResponse> reportResponse = ReportMapper.convertViewListToDto(reportResponses);
        return reportResponse;
    }
    @Override
    public List<ReportResponse> getAllReportsOptimised(User user, String latitude, String longitude, List<String> dogState, List<String> dogMentalState, String distance, Long pageCount, Long pageSize){
//        List<ReportResponseView> reportResponses = reportRepository.getReportsFilteredByLocation(user.getId(), Float.valueOf(latitude), Float.valueOf(longitude));
//        List<ReportResponse> reportResponse = ReportMapper.convertViewListToDto(reportResponses);
        Specification<Report> spec = ReportSpecification.filterReport(Float.valueOf(latitude),
                Float.valueOf(longitude),
                Float.valueOf(distance),
                 dogState,
                dogMentalState,
                user != null?user.getId():null);

        // Create a Pageable object with pagination and sorting
        Pageable pageable = PageRequest.of(Math.toIntExact(pageCount), Math.toIntExact(pageSize));
        Page<Report> reports = reportRepository.findAll(spec, pageable);
        List<Report> reportsList = reports.getContent();
        List<ReportResponse> responseList = reportsList.stream().map(ReportMapper::reportResponseDtoFromEntity).toList();
        return responseList;
    }

    @Override
    public ReportResponse getReport(Long id) throws PawException {
        Report report = reportRepository.findById(id).orElseThrow(()->new PawException("id not found", HttpStatus.BAD_REQUEST));
        return ReportMapper.reportResponseDtoFromEntity(report);
    }

    @Override
    public Long getCountByLatLongDist(User user, String latitude, String longitude, String distance) {
        return reportRepository.getCountOfReportsForAnAreaCustomDist(user.getId(), Float.valueOf(longitude), Float.valueOf(latitude), Float.valueOf(distance));
    }


    private RiskLevels getRiskLevel(Long count){
        /**
         >8-Danger
         2-8 - Moderate
         0-2 - Safe
         **/
        if(count>8){
            return RiskLevels.DANGER;
        }
        if(count>2 && count<=8){
            return RiskLevels.MODERATE_THREAT;
        }
        return RiskLevels.SAFE;

    }
    @Override
    public DangerNotify dangerNotifier(User user, String latitude, String longitude, String distance){
        Long count = getCountByLatLongDist(user, latitude, longitude, distance);
        RiskLevels riskLevel = getRiskLevel(count);
        return DangerNotify.builder()
                .count(count)
                .riskLevel(riskLevel)
                .build();
    }


    @Override
    public ReportResponse updateDogsStatus(Long id, String dogAttendedStateValue) throws PawException {
        DogAttendedState dogAttendedState = null;
        try{
            dogAttendedState = DogAttendedState.valueOf(dogAttendedStateValue);
        }catch (IllegalArgumentException e){
            throw new PawException("not correct attended state", HttpStatus.BAD_REQUEST);
        }
        Report reportResponse = reportRepository.findById(id).orElseThrow(()->new PawException("Invalide Dog Report Id", HttpStatus.BAD_REQUEST));
        reportResponse.setDogState(dogAttendedState.name());
        reportResponse = reportRepository.save(reportResponse);
        return ReportMapper.reportResponseDtoFromEntity(reportResponse);
    }

    @Override
    public void deleteReport(User user, Long id) throws PawException {
        Report report = reportRepository.findById(id).orElseThrow(()->new PawException("ID Doesnt Exist", HttpStatus.BAD_REQUEST));
        if(!user.getId().equals(report.getUser().getId())){
            throw new PawException("user not same so no delete access",HttpStatus.BAD_REQUEST);
        }
        reportRepository.deleteById(id);
    }
}
