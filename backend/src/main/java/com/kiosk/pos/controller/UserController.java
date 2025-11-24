package com.kiosk.pos.controller;


import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.UserDto;
import com.kiosk.pos.service.UserService;
import com.kiosk.pos.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getUserProfile(
            @RequestHeader("Authorization") String jwt
    ) throws UserException {
        User user = userService.getUserFromJWT(jwt);
        return ResponseEntity.ok(AuthServiceImpl.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable("id") Long id
    ) throws UserException {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(AuthServiceImpl.toDto(user));
    }
}
