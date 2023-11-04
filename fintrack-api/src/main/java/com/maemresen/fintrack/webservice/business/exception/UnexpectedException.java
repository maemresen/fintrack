package com.maemresen.fintrack.webservice.business.exception;

import com.maemresen.fintrack.webservice.business.exception.enums.ExceptionType;

public class UnexpectedException extends ServiceException {
    public UnexpectedException(Throwable throwable) {
        super(throwable, ExceptionType.UNEXPECTED);
    }
}

