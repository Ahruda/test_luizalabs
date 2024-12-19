package com.luizalabs.orders.infrastructure.exception.handler;

import com.luizalabs.orders.domain.exception.FileInvalidException;
import com.luizalabs.orders.domain.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandlerAdvice {

    @ExceptionHandler(OrderNotFoundException.class)
    ResponseEntity<?> orderNotFoundException(OrderNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(FileInvalidException.class)
    ResponseEntity<?> fileInvalidException(FileInvalidException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<?> buildResponse(final HttpStatus statusCode, final String cause) {
        Map<String, Object> body = new HashMap<>();

        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", statusCode.value());
        body.put("error", statusCode.getReasonPhrase());
        body.put("message", cause);

        return ResponseEntity.status(statusCode).body(body);
    }
}
