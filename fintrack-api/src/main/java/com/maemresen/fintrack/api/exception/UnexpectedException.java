package com.maemresen.fintrack.api.exception;

import com.maemresen.fintrack.api.exception.enums.ExceptionType;

public class UnexpectedException extends ServiceException {
    public UnexpectedException(Throwable throwable) {
        super(throwable, ExceptionType.UNEXPECTED);
    }
}

