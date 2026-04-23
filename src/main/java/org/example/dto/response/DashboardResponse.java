package org.example.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private Long userId;
    private String fullName;
    private Long daysClean;
    private Integer totalAchievements;
    private Integer activeHabitsCount;
    private List<HabitResponse> habits;
    private List<AchievementResponse> recentAchievements;
    private List<RelapseResponse> recentRelapses;
}