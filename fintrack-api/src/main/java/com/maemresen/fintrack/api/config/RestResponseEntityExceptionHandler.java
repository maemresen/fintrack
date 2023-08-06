package com.maemresen.fintrack.api.config;

import com.maemresen.fintrack.api.exceptions.ServiceException;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import com.maemresen.fintrack.api.utils.constants.HeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<Object> handleServiceException(ServiceException serviceException, WebRequest request) {
		ExceptionType exceptionType = serviceException.getExceptionType();
		return ResponseEntity.status(exceptionType.getHttpStatus())
				.header(HeaderConstants.ERROR_CODE_HEADER, exceptionType.getCode())
				.body(serviceException.getData());
	}
}