package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementRequest {

    @NotBlank(message = "El título del logro es obligatorio")
    private String title;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String description;

    @NotBlank(message = "El icono es necesario para la vista de progreso")
    private String icon;

    @NotBlank(message = "La categoría ayuda a organizar las medallas")
    private String category;
}