package com.appsdeveloperblog.apps.ws.restfulwebservices.exceptions;

import org.springframework.http.HttpStatus;

public class UserPersistenceException extends RuntimeException {

    public UserPersistenceException(String message, HttpStatus httpStatus) {
        super(message);
    }
}
