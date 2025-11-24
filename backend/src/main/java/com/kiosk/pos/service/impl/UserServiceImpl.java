package com.kiosk.pos.service.impl;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.configuration.JWTProvider;
import com.kiosk.pos.model.User;
import com.kiosk.pos.repository.UserRepository;
import com.kiosk.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    @Override
    public User getUserFromJWT(String jwt) throws UserException {
        String email = jwtProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(Objects.isNull(user)) throw new UserException("Invalid Token");
        return user;
    }

    @Override
    public User getCurrentuser() throws UserException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }

    @Override
    public User getUserByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if(Objects.isNull(user)) throw new UserException("User not found");
        return user;
    }

    @Override
    public User getUserById(Long id) throws UserException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
