package com.kiosk.pos.payload.response;


import com.kiosk.pos.payload.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String jwt;
    private String message;
    private UserDto user;
}
