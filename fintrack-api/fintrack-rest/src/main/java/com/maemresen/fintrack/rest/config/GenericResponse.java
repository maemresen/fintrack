package com.maemresen.fintrack.rest.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.maemresen.fintrack.domain.error.code.CommonErrorCode;
import com.maemresen.fintrack.domain.error.exception.ServiceException;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenericResponse<T> {
	private LocalDateTime timestamp;
	private String message;
	private T data;

	@JsonInclude(value = JsonInclude.Include.NON_NULL)
	private String errorCode;

	public static <R> GenericResponse<R> ok(R data) {
		return GenericResponse.<R>builder()
			.timestamp(LocalDateTime.now())
			.message("Success")
			.data(data)
			.build();
	}


	private static <R> GenericResponse<R> error(String errorCode, R data) {
		return GenericResponse.<R>builder()
			.timestamp(LocalDateTime.now())
			.message("Error")
			.data(data)
			.errorCode(errorCode)
			.build();
	}

	public static GenericResponse<Object> error(ServiceException serviceException) {
		return error(serviceException.getCode(), serviceException.getData());
	}

	public static GenericResponse<Object> error(CommonErrorCode commonErrorCode, Object data) {
		return error(commonErrorCode.getCode(), data);
	}

	public static GenericResponse<Object> error(CommonErrorCode commonErrorCode) {
		return error(commonErrorCode, null);
	}
}
