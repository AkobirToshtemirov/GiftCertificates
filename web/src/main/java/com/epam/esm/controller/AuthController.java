package com.epam.esm.controller;

import com.epam.esm.dto.MessageDTO;
import com.epam.esm.dto.TokenRequest;
import com.epam.esm.dto.TokenResponse;
import com.epam.esm.dto.UserRegisterDTO;
import com.epam.esm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth", produces = "application/json", consumes = "application/json")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> generateToken(@Valid @RequestBody TokenRequest request) {
        TokenResponse tokenResponse = userService.generateToken(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageDTO> createUser(@Valid @RequestBody UserRegisterDTO dto) {
        return ResponseEntity.status(201).body(userService.register(dto));
    }
}