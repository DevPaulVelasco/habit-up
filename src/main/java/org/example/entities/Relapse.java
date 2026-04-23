package org.example.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "relapses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Relapse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- INFORMACIÓN DE LA VISTA (Frontend) ---

    @Column(nullable = false)
    private String reason; // Ej.: "Ansiedad", "Estrés", "Entorno Social"

    @Column(length = 1000)
    private String comment; // Espacio para que Alex se desahogue (TextArea en el Front)

    @Column(nullable = false)
    private String emotion; // Cómo se siente Alex: "Triste", "Enojado", "Decepcionado"

    // --- DATOS DE CONTROL ---

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime relapsedAt; // Fecha y hora automática del tropiezo

    // --- LA RELACIÓN ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Object getRelapsedAt(Object t) {
        return null;
    }
}