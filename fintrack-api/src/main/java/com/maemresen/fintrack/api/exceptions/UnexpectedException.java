package com.maemresen.fintrack.api.exceptions;

import com.maemresen.fintrack.api.utils.constants.ExceptionType;

public class UnexpectedException extends ServiceException {
    public UnexpectedException(Throwable throwable) {
        super(throwable, ExceptionType.UNEXPECTED);
    }
}

