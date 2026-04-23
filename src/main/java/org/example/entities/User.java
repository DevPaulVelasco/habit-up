package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // Atributos originales para Perfil, SOS y Dashboard
    private String bio;
    private String avatarUrl;
    private String location;
    private String phoneNumber;

    @Builder.Default
    private Boolean isPrivate = false;

    @Builder.Default
    private Boolean notificationsEnabled = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Relaciones directas
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Habit> habits = new ArrayList<>();

    // Trofeos directos del usuario
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Achievement> achievements = new ArrayList<>();
}