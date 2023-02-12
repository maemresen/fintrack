package com.maemresen.fintrack.domain.error.exception;

import com.maemresen.fintrack.domain.error.code.CommonErrorCode;
import com.maemresen.fintrack.domain.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class ServiceException extends Exception {
	private final ErrorCode errorCode;
	private Object data;

	// ...
	public ServiceException(String message) {
		super(message);
		this.errorCode = CommonErrorCode.SERVICE_ERROR;
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = CommonErrorCode.SERVICE_ERROR;
	}

	public ServiceException(Throwable cause) {
		super(cause);
		this.errorCode = CommonErrorCode.SERVICE_ERROR;
	}

	// ...
	 public ServiceException(String message, Object data) {
		super(message);
		this.data = data;
		 this.errorCode = CommonErrorCode.SERVICE_ERROR;
	}

	public ServiceException(Throwable cause, Object data) {
		super(cause);
		this.data = data;
		this.errorCode = CommonErrorCode.SERVICE_ERROR;
	}

	public ServiceException(String message, Throwable cause, Object data) {
		super(message, cause);
		this.data = data;
		this.errorCode = CommonErrorCode.SERVICE_ERROR;
	}

	// ...
	protected ServiceException(String message, ErrorCode errorCode, Object data) {
		super(message);
		this.errorCode = errorCode;
		this.data = data;
	}

	protected ServiceException(Throwable cause, ErrorCode errorCode, Object data) {
		super(cause);
		this.errorCode = errorCode;
		this.data = data;
	}

	protected ServiceException(String message, Throwable cause, ErrorCode errorCode, Object data) {
		super(message, cause);
		this.errorCode = errorCode;
		this.data = data;
	}

	//...
	public String getCode(){
		return this.errorCode.getCode();
	}
}
