package com.spring.chatserver.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExceptionResponse {
    private LocalDateTime errorTime;
    private String errorMessage;
    private String errorPath;
    private HttpStatus errorStatusCode;
}