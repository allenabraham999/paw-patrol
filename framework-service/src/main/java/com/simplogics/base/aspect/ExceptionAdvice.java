package com.simplogics.base.aspect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.simplogics.base.dto.BaseResponse;
import com.simplogics.base.utils.Translator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;


@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<BaseResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getAllErrors().stream().map(msg -> Translator.translateToLocale(msg.getDefaultMessage())).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
				.data(null)
				.messageCode(1)
				.message(Translator.translateToLocale("validation.errors"))
				.hasErrors(true)
				.errors(errors)
				.build());
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<BaseResponse> handleTypeMismatchExceptions(MethodArgumentTypeMismatchException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
				.data(null)
				.messageCode(1)
				.message(Translator.translateToLocale("validation.errors"))
				.hasErrors(true)
				.errors(errors)
				.build());
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<BaseResponse> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseResponse.builder()
				.data(null)
				.messageCode(1)
				.message(Translator.translateToLocale("access.denied"))
				.hasErrors(true)
				.build());
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<BaseResponse> handleNotFoundExceptions(NoResourceFoundException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());

		BaseResponse baseResponse = BaseResponse.builder()
				.status(false)
				.messageCode(1)
				.data(null)
				.hasErrors(true)
				.message(Translator.translateToLocale("api.not.found"))
				.errors(errors)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<BaseResponse> handleMethodNotFoundExceptions(HttpRequestMethodNotSupportedException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());

		BaseResponse baseResponse = BaseResponse.builder()
				.status(false)
				.data(null)
				.messageCode(1)
				.hasErrors(true)
				.message(Translator.translateToLocale("method.not.found"))
				.errors(errors)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<BaseResponse> handleMissingParameterExceptions(MissingServletRequestParameterException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
				.data(null)
				.messageCode(1)
				.message(Translator.translateToLocale("validation.errors"))
				.hasErrors(true)
				.errors(errors)
				.build());
	}


	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<BaseResponse> handleMessageNotReadableExceptions(HttpMessageNotReadableException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.builder()
				.data(null)
				.messageCode(1)
				.message(Translator.translateToLocale("request.not.readable"))
				.hasErrors(true)
				.errors(errors)
				.build());
	}

	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<BaseResponse> handleMissingPathVariableExceptions(MissingPathVariableException ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());

		BaseResponse baseResponse = BaseResponse.builder()
				.status(false)
				.messageCode(1)
				.data(null)
				.hasErrors(true)
				.message(Translator.translateToLocale("validation.errors"))
				.errors(errors)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> handleAllExceptions(Exception ex) {
		List<String> errors = new ArrayList<>();
		errors.add(ex.getMessage());

		BaseResponse baseResponse = BaseResponse.builder()
				.status(false)
				.messageCode(1)
				.data(null)
				.hasErrors(true)
				.message(Translator.translateToLocale("internal.server.error"))
				.errors(errors)
				.build();

		return new ResponseEntity<>(baseResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
