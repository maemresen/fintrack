package com.maemresen.fintrack.webservice.business.exception;

public class UnexpectedException extends ServiceException {
    public UnexpectedException(Throwable throwable) {
        super(throwable, ExceptionType.UNEXPECTED);
    }
}

