package com.spring.chatserver.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ChatServerExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e
    ) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }




    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> entityAlreadyExist(BadRequestException e, HttpServletRequest request) {
        ExceptionResponse er = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.BAD_REQUEST)
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> roleNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        ExceptionResponse er = ExceptionResponse.builder()
                .errorMessage(e.getMessage())
                .errorPath(request.getRequestURI())
                .errorStatusCode(HttpStatus.NOT_FOUND)
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
    }

}