package com.simplogics.base.aspect;

import java.lang.reflect.Method;
import java.util.Collections;

import com.simplogics.base.dto.BaseResponse;
import com.simplogics.base.exception.PawException;
import com.simplogics.base.utils.Translator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.simplogics.base.annotation.APIResult;

@Aspect
@Component
public class ResultAspect {

	private static final Logger logger = LoggerFactory.getLogger(ResultAspect.class);

	@Around("@annotation(com.simplogics.base.annotation.APIResult)")
	public Object onApiResponse(final ProceedingJoinPoint pjp) {
		final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		APIResult apiResult = method.getAnnotation(APIResult.class);
		BaseResponse result;
		HttpStatus httpStatus;
		try {
			return pjp.proceed();
		} catch (Throwable e) {
            switch (e) {
                case PawException exception -> {
                    httpStatus = exception.getStatus();
                    result = new BaseResponse(false, null, 1, Translator.translateToLocale(exception.getMessage()), true,
                            Collections.singletonList(exception.getMessage()));
                }
                case BadCredentialsException exception -> {
                    httpStatus = HttpStatus.UNAUTHORIZED;
                    result = new BaseResponse(false, null, 1, Translator.translateToLocale("incorrect.credentials"), true,
                            Collections.singletonList(exception.getMessage()));
                }
				case UsernameNotFoundException exception -> {
					httpStatus = HttpStatus.BAD_REQUEST;
					result = new BaseResponse(false, null, 1, Translator.translateToLocale(exception.getMessage()), true,
							Collections.singletonList(exception.getMessage()));
				}
                default -> {
                    logger.error("Something Went Wrong: {}", e.getMessage());
                    result = new BaseResponse(false, null, 1, Translator.translateToLocale(apiResult.error_message()), true,
							Collections.singletonList(e.getMessage()));
                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }
		}
		return new ResponseEntity<Object>(result, httpStatus);
	}
}
