package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelapseRequest {

    @NotBlank(message = "Debes seleccionar una razón para la recaída")
    private String reason; // Ej.: "Ansiedad", "Estrés", "Presión Social"

    @NotBlank(message = "Cuéntanos cómo te sientes emocionalmente")
    private String emotion; // Ej: "Triste", "Enojado", "Frustrado"

    @Size(max = 1000, message = "El comentario es muy largo, trata de resumir tus sentimientos")
    private String comment; // Texto libre opcional o descriptivo
}