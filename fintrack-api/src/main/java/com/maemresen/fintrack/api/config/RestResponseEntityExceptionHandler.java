package com.maemresen.fintrack.api.config;

import com.maemresen.fintrack.api.dto.ErrorDto;
import com.maemresen.fintrack.api.exceptions.InvalidParameterException;
import com.maemresen.fintrack.api.exceptions.NotFoundException;
import com.maemresen.fintrack.api.exceptions.ServiceException;
import com.maemresen.fintrack.api.exceptions.UnexpectedException;
import com.maemresen.fintrack.api.utils.constants.ExceptionType;
import com.maemresen.fintrack.api.utils.constants.HeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException serviceException, WebRequest request) {
        return getResponseEntity(serviceException);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ServiceException serviceException;
        if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            Object data = methodArgumentNotValidException.getFieldErrors().stream()
                    .map((Function<FieldError, Object>) fieldError -> Map.of(
                            "field", fieldError.getField(),
                            "message", Optional.ofNullable(fieldError.getDefaultMessage()).orElse(""),
                            "rejectedValue", Optional.ofNullable(fieldError.getRejectedValue()).orElse("")
                    ))
                    .toArray();
            serviceException = new InvalidParameterException(ex, data);
        } else if (HttpStatus.NOT_FOUND == statusCode) {
            serviceException = new NotFoundException(ex);
        } else if (HttpStatus.BAD_REQUEST == statusCode) {
            serviceException = new InvalidParameterException(ex);
        } else {
            serviceException = new UnexpectedException(ex);
        }

        return getResponseEntity(serviceException);
    }

    private ResponseEntity<Object> getResponseEntity(ServiceException serviceException) {
        ExceptionType exceptionType = serviceException.getExceptionType();
        var error = ErrorDto.builder()
                .message(serviceException.getMessage())
                .data(serviceException.getData())
                .stackTrace(serviceException.getStackTrace())
                .build();
        return ResponseEntity.status(exceptionType.getHttpStatus())
                .header(HeaderConstants.ERROR_CODE_HEADER, exceptionType.getCode())
                .body(error);
    }
}