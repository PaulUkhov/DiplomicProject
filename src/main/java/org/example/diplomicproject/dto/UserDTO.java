package org.example.diplomicproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для представления пользователя")
public class UserDTO {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Паша")
    private String name;

    @Schema(description = "Email пользователя", example = "pasha@example.com")
    private String email;
}

