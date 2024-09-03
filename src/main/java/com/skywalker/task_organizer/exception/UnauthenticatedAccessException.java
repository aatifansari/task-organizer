package com.skywalker.task_organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthenticatedAccessException extends RuntimeException{
    public UnauthenticatedAccessException(){}
    public UnauthenticatedAccessException(String message){super(message);}
    public UnauthenticatedAccessException(String message, Throwable cause){super(message, cause);}
}
