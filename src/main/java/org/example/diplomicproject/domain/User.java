package org.example.diplomicproject.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
@Schema(description = "Пользователь системы")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Column(name = "login", nullable = false)
    @Schema(description = "Логин пользователя", example = "pasha123")
    private String login;

    @Column(name = "email", nullable = false)
    @Schema(description = "Электронная почта пользователя", example = "pasha@example.com")
    private String email;

    @JsonIgnore
    @Schema(description = "Пароль (не сохраняется в базе)", example = "securePassword123")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список заказов пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "Корзина пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "Отзывы пользователя", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Review> reviews;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Schema(description = "Роли пользователя", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
    private Set<Role> roles = new HashSet<>();
}
