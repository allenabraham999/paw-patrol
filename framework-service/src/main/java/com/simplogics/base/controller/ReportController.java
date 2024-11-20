package com.simplogics.base.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.simplogics.base.annotation.APIResult;
import com.simplogics.base.dto.BaseResponse;
import com.simplogics.base.dto.DangerNotify;
import com.simplogics.base.dto.ReportCreateDto;
import com.simplogics.base.dto.ReportResponse;
import com.simplogics.base.entity.Report;
import com.simplogics.base.entity.User;
import com.simplogics.base.enums.RiskLevels;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.repository.ReportRepository;
import com.simplogics.base.repository.UserRepository;
import com.simplogics.base.security.authDetails.UserAuthDetails;
import com.simplogics.base.service.IReportService;
import com.simplogics.base.service.IUserService;
import com.simplogics.base.service.ReportService;
import com.simplogics.base.utils.ApiRoutes;
import com.simplogics.base.utils.ResponseUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiRoutes.REPORT)
public class ReportController extends BaseController {

    @Autowired
    IReportService reportService;
    @Autowired
    UserRepository userRepository;


    @APIResult(error_message = "broke")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> createReport( @RequestBody  @Valid ReportCreateDto reportCreateDto) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()->new PawException("fdkfke", HttpStatus.BAD_REQUEST));
        ReportResponse reportResponse = reportService.createReport(user, reportCreateDto);
        return ResponseUtil.getStatusCreatedResponseEntity(reportResponse, " ", 1);
    }

    @APIResult(error_message = "broke")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getReports(@RequestParam("lat")String latitude,
                                                   @RequestParam("long")String longitude,
                                                   @RequestParam(value = "dist", required = false) String dist,
                                                   @RequestParam(value = "risk", required = false) String risk) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()->new PawException("fdkfke", HttpStatus.BAD_REQUEST));
        List<ReportResponse> reports = reportService.getAllReports(user, latitude, longitude);
        return ResponseUtil.getStatusOkResponseEntity(reports," ", 1);
    }
    @APIResult(error_message = "broke")
    @RequestMapping(path = "/optimised", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getReportsOptimised(@RequestParam("lat")String latitude,
                                                            @RequestParam("long")String longitude,
                                                            @RequestParam("dist") String dist,
                                                            @RequestParam(value = "dogState", required = false) List<String> dogState,
                                                            @RequestParam(value = "dogMentalState", required = false) List<String> dogMentalState,
                                                            @RequestParam(value = "pageCount", required = true) Long pageCount,
                                                            @RequestParam(value = "pageSize", required = true)Long pageSize) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()->new PawException("fdkfke", HttpStatus.BAD_REQUEST));
        List<ReportResponse> reports = reportService.getAllReportsOptimised(user, latitude, longitude, dogState, dogMentalState, dist, pageCount, pageSize);
        return ResponseUtil.getStatusOkResponseEntity(reports," ", 1);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @APIResult(error_message = "broke")
    @RequestMapping(path = "/optimised-admin", method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getReportsOptimisedForAdmin(@RequestParam("lat")String latitude, @RequestParam("long")String longitude, @RequestParam("dist") String dist, @RequestParam(value = "dogState", required = false) List<String> dogState,@RequestParam(value = "dogMentalState", required = false) List<String> dogMentalState, @RequestParam(value = "pageCount", required = false) Long pageCount, @RequestParam(value = "pageSize", required = false)Long pageSize) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()->new PawException("fdkfke", HttpStatus.BAD_REQUEST));
        List<ReportResponse> reports = reportService.getAllReportsOptimised(null, latitude, longitude, dogState, dogMentalState, dist, pageCount, pageSize);
        return ResponseUtil.getStatusOkResponseEntity(reports," ", 1);
    }

    @APIResult(error_message = "broke")
    @RequestMapping(path = ApiRoutes.ID,method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getReport(@PathVariable("id")String idValue) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        Long id = Long.valueOf(idValue);
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()->new PawException("fdkfke", HttpStatus.BAD_REQUEST));
        ReportResponse report = reportService.getReport(id);
        return ResponseUtil.getStatusOkResponseEntity(report," ", 1);
    }
    @APIResult(error_message = "broke")
    @RequestMapping(path = ApiRoutes.NOTIFY,method = RequestMethod.GET)
    public ResponseEntity<BaseResponse> getNotified(@RequestParam("lat")String latitude, @RequestParam("long")String longitude, @RequestParam("dist")String dist) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()->new PawException("fdkfke", HttpStatus.BAD_REQUEST));
        DangerNotify danger = reportService.dangerNotifier(user, latitude, longitude, dist);
        return ResponseUtil.getStatusOkResponseEntity(danger," ", 1);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @APIResult(error_message = "broke")
    @RequestMapping(path = ApiRoutes.DOG_STATUS+ApiRoutes.ID , method= RequestMethod.PATCH)
    public ResponseEntity<BaseResponse> changeDogReportStatus(@PathVariable("id") String id, @RequestParam("dogStatus") String dogStatus) throws PawException {
        ReportResponse response = reportService.updateDogsStatus(Long.valueOf(id), dogStatus);
        return ResponseUtil.getStatusCreatedResponseEntity(response, " ", 1);
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @APIResult(error_message = "broke")
    @RequestMapping(path = ApiRoutes.ID, method = RequestMethod.DELETE)
    public ResponseEntity<BaseResponse> deleteReport(@PathVariable("id") Long id) throws PawException {
        UserAuthDetails userAuthDetails = BaseController.getUserDetailsByPrincipal();
        User user = userRepository.findById(userAuthDetails.getId()).orElseThrow(()-> new PawException("id not found", HttpStatus.BAD_REQUEST));
        reportService.deleteReport(user, id);
        return ResponseUtil.getStatusCreatedResponseEntity(null, "deleted succesfully", 1);
    }
}
