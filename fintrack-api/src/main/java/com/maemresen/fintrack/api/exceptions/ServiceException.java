package com.maemresen.fintrack.api.exceptions;

import javax.net.ssl.ExtendedSSLSession;

public class ServiceException extends BusinessException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
