package org.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelapseResponse {

    private Long id;

    // --- DATOS PARA LAS TARJETAS DEL HISTORIAL ---
    private String reason;    // Ej.: "Presión Social"
    private String emotion;   // Ej: "Frustrado"
    private String comment;   // El texto de desahogo de Alex

    // --- DATOS TEMPORALES ---
    private LocalDateTime relapsedAt; // Para que React diga "Hace 2 días"

    // --- METADATOS DE APOYO ---
    private Long userId;
    private String username; // Por si quieres mostrar "Alex registró un tropiezo"
    private String description;
}