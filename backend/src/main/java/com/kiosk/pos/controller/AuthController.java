package com.kiosk.pos.controller;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.payload.dto.UserDto;
import com.kiosk.pos.payload.response.AuthResponse;
import com.kiosk.pos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> signUp(@RequestBody UserDto user) throws UserException {
        return ResponseEntity.ok(authService.signup(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody UserDto user) throws UserException {
        return ResponseEntity.ok(authService.login(user));
    }

}
