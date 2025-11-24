package com.kiosk.pos.service;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.payload.dto.UserDto;
import com.kiosk.pos.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse signup(UserDto user) throws UserException;
    AuthResponse login(UserDto user) throws UserException;
}
