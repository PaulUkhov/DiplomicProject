package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "roles")
@Schema(description = "Роль пользователя в системе (например, ROLE_USER, ROLE_ADMIN)")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор роли", example = "1")
    private Long id;

    @Column(name = "name")
    @Schema(description = "Название роли", example = "ROLE_USER")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Schema(description = "Пользователи, связанные с этой ролью", hidden = true)
    private Set<User> users = new HashSet<>();
}
