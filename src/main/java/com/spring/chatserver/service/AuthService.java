package com.spring.chatserver.service;

import com.spring.chatserver.dto.request.LoginRequest;
import com.spring.chatserver.dto.request.SignUpRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest);

    ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest);
}
