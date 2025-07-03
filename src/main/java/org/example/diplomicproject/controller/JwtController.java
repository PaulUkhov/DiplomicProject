package org.example.diplomicproject.controller;

import jakarta.validation.Valid;
import org.example.diplomicproject.dto.RegisterRequest;
import org.example.diplomicproject.security.AuthenticationRequest;
import org.example.diplomicproject.security.AuthenticationResponse;
import org.example.diplomicproject.service.AuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Аутентификация", description = "Регистрация и вход с JWT")
public class JwtController {

    private final AuthUserService authUserService;

    public JwtController(AuthUserService authUserService) {
        this.authUserService = authUserService;
    }

    @Operation(summary = "Регистрация пользователя", description = "Создаёт нового пользователя и возвращает результат")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Пользователь с таким email уже существует")
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class)))
            @Valid @RequestBody RegisterRequest request) {

        String result = authUserService.registerUser(request);
        if (result.contains("существует")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Аутентификация", description = "Вход пользователя и получение JWT токена")
    @ApiResponse(responseCode = "200", description = "Аутентификация успешна, возвращён токен")
    @ApiResponse(responseCode = "401", description = "Неверный логин или пароль")
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для входа (login и пароль)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class)))
            @Valid @RequestBody AuthenticationRequest authRequest) {

        try {
            AuthenticationResponse response = authUserService.authenticateAndGetTokens(
                    authRequest.getLogin(),
                    authRequest.getPassword()
            );
            return ResponseEntity.ok(AuthenticationResponse.builder().token(response.getToken()).build());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }
}


