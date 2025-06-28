package org.example.diplomicproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Schema(description = "Запрос на регистрацию нового пользователя")
public class RegisterRequest {

    @NotBlank(message = "Login is mandatory")
    @Schema(description = "Уникальный логин пользователя", example = "pasha123", required = true)
    private String login;

    @Email(message = "Email is not valid")
    @NotBlank(message = "Email is mandatory")
    @Schema(description = "Адрес электронной почты", example = "pasha@example.com", required = true)
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be at least 8 characters")
    @Schema(description = "Пароль (минимум 8 символов)", example = "securePassword123", required = true)
    private String password;
}


