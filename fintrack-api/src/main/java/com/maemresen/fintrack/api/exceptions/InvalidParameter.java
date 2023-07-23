package com.maemresen.fintrack.api.exceptions;

public class InvalidParameter extends BusinessException{
    public InvalidParameter(String message) {
        super(message);
    }

    public InvalidParameter(String message, Throwable cause) {
        super(message, cause);
    }


}
