package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private String description;

    @NotBlank(message = "La categoría es necesaria")
    private String category;

    // Cambiado a LocalDateTime para precisión de segundos en WebSockets

    private LocalDateTime startDate;

    @NotNull(message = "El gasto diario es necesario")
    @PositiveOrZero(message = "El gasto no puede ser negativo")
    private Double dailyExpense;

    @Builder.Default
    private String currency = "MXN";

    private Integer goalDays;
}