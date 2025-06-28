package org.example.diplomicproject.domain;

import jakarta.persistence.*;
import lombok.*;
import org.example.diplomicproject.config.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
@Table(name = "reviews")
public class Review extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rating", nullable = false, length = 50)
    private int rating;
    @Column(name = "comment", length = 500)
    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;


}
