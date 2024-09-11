package com.skywalker.task_organizer.config;

import com.skywalker.task_organizer.exception.BadRequestException;
import com.skywalker.task_organizer.exception.EntityNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));

        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);

        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception){
        ProblemDetail errorDetail = null;
        exception.printStackTrace();

        if(exception instanceof BadCredentialsException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty("description", "Username or password is incorrect");
            return errorDetail;
        }

        if(exception instanceof AccountStatusException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "Account is locked");
        }

        if(exception instanceof AccessDeniedException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "Unauthorized access");
        }

        if(exception instanceof SignatureException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "JWT signature is invalid");
        }

        if(exception instanceof ExpiredJwtException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "JWT token has expired");
        }

        if(exception instanceof BadRequestException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
            errorDetail.setProperty("reason", "Bad Request");
        }

        if(exception instanceof EntityNotFoundException){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
            errorDetail.setProperty("reason", "Resource not found");
        }

        if(exception instanceof DataIntegrityViolationException){
            if(exception.getMessage().contains("assignee_id")){
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
                errorDetail.setProperty("reason", "Invalid Assignee");
            }else if(exception.getMessage().contains("email")){
                errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
                errorDetail.setProperty("reason", "Duplicate Email");
            }else {
                errorDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                errorDetail.setProperty("reason", "Check Request Body");
            }
        }

        if(errorDetail == null){
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            errorDetail.setProperty("description", " Unknown internal server error");
        }
        return errorDetail;
    }
}
