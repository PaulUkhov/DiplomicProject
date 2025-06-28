package org.example.diplomicproject.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.diplomicproject.domain.User;
import org.example.diplomicproject.dto.RegisterRequest;
import org.example.diplomicproject.repository.UserRepository;
import org.example.diplomicproject.security.AuthenticationRequest;
import org.example.diplomicproject.security.AuthenticationResponse;
import org.example.diplomicproject.security.JwtUtil;
import org.example.diplomicproject.service.AuthUserService;
import org.example.diplomicproject.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/auth")
public class JwtController {

    private final AuthUserService authUserService;

    public JwtController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    // 🔐 Регистрация
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String result = authUserService.registerUser(request);
        if (result.contains("существует")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    // 🔑 Аутентификация и получение access-токена
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticationRequest authRequest) {
        try {
            AuthenticationResponse response = authUserService.authenticateAndGetTokens(
                    authRequest.getEmail(),
                    authRequest.getPassword()
            );
            String token = response.getToken();
            return ResponseEntity.ok(AuthenticationResponse.builder().token(token).build());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }
}

