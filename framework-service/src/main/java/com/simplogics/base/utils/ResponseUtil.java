package com.simplogics.base.utils;

import com.simplogics.base.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<BaseResponse> getStatusOkResponseEntity(Object dto, String messageCode, int showMessage) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(true)
                .messageCode(showMessage)
                .data(dto)
                .hasErrors(false)
                .message(Translator.translateToLocale(messageCode))
                .errors(null)
                .build());
    }

    public static ResponseEntity<BaseResponse> getStatusCreatedResponseEntity(Object dto, String messageCode, int showMessage) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.builder()
                .status(true)
                .messageCode(showMessage)
                .data(dto)
                .hasErrors(false)
                .message(Translator.translateToLocale(messageCode))
                .errors(null)
                .build());
    }

}
