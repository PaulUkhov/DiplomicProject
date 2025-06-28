package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.diplomicproject.config.Auditable;
import java.util.ArrayList;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Schema(description = "Сущность заказа")
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор заказа", example = "1")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Статус заказа", example = "PENDING")
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Пользователь, сделавший заказ")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Список товаров в заказе")
    private List<OrderItem> items = new ArrayList<>();
}

