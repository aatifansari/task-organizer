package com.skywalker.task_organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Operation not allowed")
public class InvalidOperationException extends RuntimeException{
    public InvalidOperationException(){}
}
