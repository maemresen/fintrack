package com.maemresen.fintrack.domain.error.exception;

import com.maemresen.fintrack.domain.error.code.ErrorCode;

public class BusinessException extends ServiceException {

	protected BusinessException(String message, ErrorCode errorCode) {
		super(message, errorCode, null);
	}
	protected BusinessException(String message, ErrorCode errorCode, Object data) {
		super(message, errorCode, data);
	}

	protected BusinessException(Throwable cause, ErrorCode errorCode) {
		super(cause, errorCode, null);
	}
	protected BusinessException(Throwable cause, ErrorCode errorCode, Object data) {
		super(cause, errorCode, data);
	}

	protected BusinessException(String message, Throwable cause, ErrorCode errorCode) {
		super(message, cause, errorCode, null);
	}

	protected BusinessException(String message, Throwable cause, ErrorCode errorCode, Object data) {
		super(message, cause, errorCode, data);
	}
}
