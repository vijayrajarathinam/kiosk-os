package com.kiosk.pos.service.impl;

import com.kiosk.pos.Exception.UserException;
import com.kiosk.pos.configuration.JWTProvider;
import com.kiosk.pos.domain.UserRole;
import com.kiosk.pos.model.User;
import com.kiosk.pos.payload.dto.UserDto;
import com.kiosk.pos.payload.response.AuthResponse;
import com.kiosk.pos.repository.UserRepository;
import com.kiosk.pos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserImplementation userImplementation;

    @Override
    public AuthResponse signup(UserDto userDto) throws UserException {
        User user = userRepository.findByEmail(userDto.getEmail());

        if(user != null)
            throw new UserException("email id already registered");

        if(userDto.getRole().equals(UserRole.ROLE_ADMIN))
            throw new UserException("Role admin is not allowed");

        User newUser = User.builder()
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .fullName(userDto.getFullName())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .lastLogin(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        Authentication authentication = authenticate(userDto.getEmail(), userDto.getPassword());
        String jwt = jwtProvider.generateToken(authentication);

        return AuthResponse.builder().jwt(jwt).user(toDto(newUser)).message("User register successfully").build();
    }

    public static UserDto toDto(User user){
        return UserDto.builder()
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthResponse login(UserDto userDto) throws UserException {
        String email = userDto.getEmail();
        String password = userDto.getPassword();
        Authentication authentication = authenticate(email, password);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().next().getAuthority();
        String jwt = jwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(email);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return AuthResponse.builder().jwt(jwt).user(toDto(user)).message("User Login successfully").build();
    }

    private Authentication authenticate(String email, String password) throws UserException {
        UserDetails userDetails = userImplementation.loadUserByUsername(email);

        if(Objects.isNull(userDetails))
            throw new UserException("Email doesn't exist "+ email);
        if(!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new UserException("Password doesn't match");

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }
}
