package com.simplogics.base.service;

import com.simplogics.base.dto.DangerNotify;
import com.simplogics.base.dto.ReportCreateDto;
import com.simplogics.base.dto.ReportResponse;
import com.simplogics.base.entity.User;
import com.simplogics.base.exception.PawException;

import java.util.List;

public interface IReportService {

    ReportResponse createReport(User user, ReportCreateDto reportCreateDto);

    List<ReportResponse> getAllReports(User user, String latitude, String longitude);

//    List<ReportResponse> getAllReportsOptimised(User user, String latitude, String longitude, String distance, Long pageCount, Long pageSize);

    List<ReportResponse> getAllReportsOptimised(User user, String latitude, String longitude, List<String> dogState, List<String> dogMentalState, String distance, Long pageCount, Long pageSize);

    ReportResponse getReport(Long id) throws PawException;
    Long getCountByLatLongDist(User user, String latitude, String longitude, String distance);

    DangerNotify dangerNotifier(User user, String latitude, String longitude, String distance);

    ReportResponse updateDogsStatus(Long id, String dogAttendedStateValue) throws PawException;

    void deleteReport(User user, Long id) throws PawException;
}
