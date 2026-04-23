package org.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;            // Código HTTP (400, 404, 500)
    private String message;       // Mensaje amigable para Alex
    private LocalDateTime timestamp;
    private Map<String, String> errors; // Para errores de validación (ej.: "password demasiado inseguro")
}