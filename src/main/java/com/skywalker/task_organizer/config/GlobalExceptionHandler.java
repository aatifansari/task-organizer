package com.skywalker.task_organizer.config;

import com.skywalker.task_organizer.exception.BadRequestException;
import com.skywalker.task_organizer.exception.EntityNotFoundException;
import com.skywalker.task_organizer.exception.UnauthenticatedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<Map<String,Object>> handleEntityNotFoundException(EntityNotFoundException e){
        Map<String,Object> map = new LinkedHashMap<>();
        map.put("message", "Entity not found");
        map.put("reason", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException e){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", "Bad Request");
        map.put("reason", e.getMessage());
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }
}
