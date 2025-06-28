package org.example.diplomicproject.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Builder
@Getter
@Setter
@Schema(description = "Ответ с access и refresh токенами после успешной аутентификации")
public class AuthenticationResponse {

    @Schema(description = "JWT access токен", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "JWT refresh токен", example = "dGhpc2lzYXJlZnJlc2h0b2tlbg==")
    private String refreshToken;
}
