package com.maemresen.fintrack.business.exception;

import com.maemresen.fintrack.business.exception.enums.ExceptionType;

public class UnexpectedException extends ServiceException {
    public UnexpectedException(Throwable throwable) {
        super(throwable, ExceptionType.UNEXPECTED);
    }
}

