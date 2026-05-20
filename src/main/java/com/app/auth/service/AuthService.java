package com.app.auth.service;

import com.app.auth.dto.AuthDtos.*;
import com.app.auth.entity.User;
import com.app.auth.repository.UserRepository;
import com.app.auth.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Username already taken");
        }
        String bcrypt = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .name(request.getName())
                .favFilms(request.getFavFilms())
                .friend(request.getFriend())
                .password(bcrypt)
                .build();
        User saved = userRepository.save(user);
        return buildResponse(jwtService.generateToken(saved), saved);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByName(request.getName())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        log.debug("Login attempt for user: {}", request.getName());
        log.debug("User found in DB: {}", user.getName());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        log.info("User '{}' logged in successfully", user.getName());
        return buildResponse(jwtService.generateToken(user), user);
    }

    public AuthResponse refresh(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new BadCredentialsException("Refresh token expired or invalid");
        }
        return buildResponse(jwtService.generateToken(user), user);
    }

    // ── único buildResponse ───────────────────────────────────
    private AuthResponse buildResponse(String token, User user) {
        long expiresInSeconds = jwtService.getExpirationMs() / 1000;
        String refreshToken = jwtService.generateRefreshToken(user);
        UserInfo info = new UserInfo(user.getId(), user.getName(), user.getFavFilms(), user.getFriend());
        return new AuthResponse(token, refreshToken, expiresInSeconds, info);
    }
}