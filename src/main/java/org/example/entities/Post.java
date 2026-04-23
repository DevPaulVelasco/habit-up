package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El contenido del post no puede estar vacío")
    @Column(length = 500) // Para que no escriban la Biblia, máximo 500 caracteres
    private String content;

    @Builder.Default
    private Integer likes = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // RELACIÓN: Muchos posts pertenecen a UN usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}