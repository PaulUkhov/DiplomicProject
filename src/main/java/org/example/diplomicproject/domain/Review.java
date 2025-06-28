package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.diplomicproject.config.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import io.swagger.v3.oas.annotations.media.Schema;


@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
@Table(name = "reviews")
@Schema(description = "Сущность отзыва на продукт")
public class Review extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор отзыва", example = "1")
    private Long id;

    @Column(name = "rating", nullable = false, length = 50)
    @Schema(description = "Оценка продукта", example = "5")
    private int rating;

    @Column(name = "comment", length = 500)
    @Schema(description = "Комментарий к продукту", example = "Отличный товар, рекомендую!")
    private String comment;

    @ManyToOne
    @Schema(description = "Пользователь, оставивший отзыв")
    private User user;

    @ManyToOne
    @Schema(description = "Продукт, на который написан отзыв")
    private Product product;
}

