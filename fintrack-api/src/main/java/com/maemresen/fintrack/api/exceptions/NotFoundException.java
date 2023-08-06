package com.maemresen.fintrack.api.exceptions;

import com.maemresen.fintrack.api.utils.constants.ExceptionType;

public class NotFoundException extends ServiceException {
    public NotFoundException(String message, Object data) {
        super(message, data, ExceptionType.NOT_FOUND);
    }

    public NotFoundException(Throwable cause) {
        super(cause, ExceptionType.NOT_FOUND);
    }
}
