package com.appsdeveloperblog.apps.ws.restfulwebservices.exceptions;

import org.springframework.http.HttpStatus;

public class UserServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    public UserServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
