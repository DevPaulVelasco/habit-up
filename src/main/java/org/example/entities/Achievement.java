package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String icon;

    @Column(nullable = false)
    private String category;

    @Column(name = "required_days", nullable = false)
    private Integer requiredDays;

    // 🔥 Agregado para que AchievementService deje de marcar error en .unlockedAt()
    private LocalDateTime unlockedAt;

    // 🔥 Relación necesaria para que el Repositorio funcione (findByUser_Id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // Método corregido para que la relación sea bidireccional
    public void setUser(User user) {
        this.user = user;
    }
}