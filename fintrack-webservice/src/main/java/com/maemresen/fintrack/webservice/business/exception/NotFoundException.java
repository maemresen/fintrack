package com.maemresen.fintrack.webservice.business.exception;

public class NotFoundException extends ServiceException {
    public NotFoundException(String message, Object data) {
        super(message, data, ExceptionType.NOT_FOUND);
    }

    public NotFoundException(Throwable cause) {
        super(cause, ExceptionType.NOT_FOUND);
    }
}
