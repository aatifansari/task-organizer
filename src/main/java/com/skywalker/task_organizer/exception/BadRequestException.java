package com.skywalker.task_organizer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid ID")
public class BadRequestException extends RuntimeException{
    public BadRequestException(){}
}
