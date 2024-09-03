package com.skywalker.task_organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Unauthorized Access")
public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(){}
    public UnauthorizedAccessException(String message){super(message);}
    public UnauthorizedAccessException(String message, Throwable cause){super(message, cause);}
}
