package org.example.diplomicproject.service;

import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.dto.RegisterRequest;
import org.example.diplomicproject.repository.UserRepository;
import org.example.diplomicproject.security.AuthenticationResponse;
import org.example.diplomicproject.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthUserService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            return "Пользователь с таким логином уже существует";
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return "Пользователь с таким email уже существует";
        }

        User user = new User();
        user.setLogin(request.getLogin());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "Пользователь успешно зарегистрирован";
    }

    //  Возвращает сразу оба токена
    public AuthenticationResponse authenticateAndGetTokens(String login, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            return AuthenticationResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (BadCredentialsException ex) {
            throw new RuntimeException("Неверный логин или пароль");
        }
    }

    //  Обновление access токена по refresh
    public String refreshAccessToken(String refreshToken) {
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtUtil.isTokenExpired(refreshToken)) {
            return jwtUtil.generateToken(userDetails);
        } else {
            throw new RuntimeException("Refresh token истёк");
        }
    }
}

