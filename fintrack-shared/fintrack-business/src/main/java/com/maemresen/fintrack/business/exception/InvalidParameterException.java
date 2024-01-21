package com.maemresen.fintrack.business.exception;

import com.maemresen.fintrack.business.exception.enums.ExceptionType;

public class InvalidParameterException extends ServiceException {

    public InvalidParameterException(String message) {
        super(message, ExceptionType.INVALID_PARAMETER);
    }

    public InvalidParameterException(Throwable cause) {
        super(cause, ExceptionType.INVALID_PARAMETER);
    }

    public InvalidParameterException(Throwable cause, Object data) {
        super(cause, ExceptionType.INVALID_PARAMETER, data);
    }
}
