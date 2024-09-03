package com.skywalker.task_organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid ID")
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(){}
    public EntityNotFoundException(String message) { super(message); }
    public EntityNotFoundException(String message, Throwable cause) { super(message, cause);}
}
