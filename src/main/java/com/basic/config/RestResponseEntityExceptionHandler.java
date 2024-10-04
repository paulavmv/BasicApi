package com.basic.config;

import com.basic.dto.output.GenericResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value
            = { IllegalArgumentException.class, IllegalStateException.class })
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        logger.error("An error occurred when processing the request", ex);
        String bodyOfResponse = "A problem occurred when handling the request";
        var response = new GenericResponse.Builder().
                success(false)
                .message(bodyOfResponse)
                .build();
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value
            = { NoSuchElementException.class })
    protected ResponseEntity<Object> handleNotFound(
            RuntimeException ex, WebRequest request) {
        logger.error("An error occurred when processing the request", ex);

        String bodyOfResponse = "Object was not found";
        var response = new GenericResponse.Builder().
                success(false)
                .message(bodyOfResponse)
                .build();
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = { RuntimeException.class, Exception.class })
    protected ResponseEntity<Object> handleGenericError(
            RuntimeException ex, WebRequest request) {
        logger.error("An error occurred when processing the request", ex);

        String bodyOfResponse = "This request couldn't be processed";
        var response = new GenericResponse.Builder().
                success(false)
                .message(bodyOfResponse)
                .build();
        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(value
            = { ConstraintViolationException.class })
    protected ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {
        logger.error("An error occurred when processing the request", ex);

        String bodyOfResponse = "This request couldn't be processed due to validation errors";
        var errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        var response = new GenericResponse.Builder().
                success(false)
                .message(bodyOfResponse)
                .data(errors)
                .build();

        return handleExceptionInternal(ex, response,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
