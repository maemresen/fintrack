package com.maemresen.fintrack.rest.config;

import com.maemresen.fintrack.domain.error.code.CommonErrorCode;
import com.maemresen.fintrack.domain.error.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		String bodyOfResponse = "This should be application specific";
		return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
		GenericResponse<Object> errorResponse;

		log.error("An error occurred while processing request {} ", request.toString(), ex);
		if (ex instanceof ServiceException serviceException) {
			errorResponse = GenericResponse.error(serviceException);
		} else {
			errorResponse = GenericResponse.error(CommonErrorCode.UN_EXPECTED_ERROR, body);
		}
		return new ResponseEntity<>(errorResponse, headers, statusCode);
	}
}
