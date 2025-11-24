package com.kiosk.pos.service;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.model.User;

import java.util.List;

public interface UserService {
    User getUserFromJWT(String jwt) throws UserException;
    User getCurrentuser() throws UserException;
    User getUserByEmail(String email) throws UserException;
    User getUserById(Long id) throws UserException;
    List<User> getAllUsers();
}
