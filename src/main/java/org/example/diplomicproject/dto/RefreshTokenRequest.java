package org.example.diplomicproject.dto;

import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;


@Getter
@Setter
@Schema(description = "Запрос на обновление JWT токена с использованием refresh-токена")
public class RefreshTokenRequest {

    @Schema(
            description = "Refresh токен, используемый для получения нового access токена",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String refreshToken;
}
