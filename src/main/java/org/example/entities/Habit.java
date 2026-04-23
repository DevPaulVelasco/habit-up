package org.example.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "habits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del hábito es obligatorio")
    private String name;

    private String description;



    @NotBlank(message = "La categoría es necesaria para los iconos")
    private String category;

    // Volvemos a LocalDateTime para que el WebSocket sepa los segundos exactos
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime startDate;

    @Column(nullable = false)
    private Double dailyExpense;

    private Double totalSaved;

    @Builder.Default
    @Column(nullable = false)
    private String currency = "MXN";

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    private Integer currentStreak = 0; // Se asegura que empiece en 0 y no en null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Para el WebSocket: Calcula los segundos totales desde que inició el hábito.
     * Con esto el cronómetro en el Front puede avanzar en tiempo real.
     */
    @Transient
    public long getSecondsClean() {
        if (startDate == null) return 0;
        return Duration.between(startDate, LocalDateTime.now()).getSeconds();
    }
}