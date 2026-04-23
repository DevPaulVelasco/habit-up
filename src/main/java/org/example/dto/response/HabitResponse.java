package org.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer currentStreak;
    private Long daysClean;       // Recibe el cálculo de días del Service
    private Double totalSaved;
    private Double dailyExpense;
    private LocalDateTime startDate;
    private String currency;
    private Boolean isActive;
}