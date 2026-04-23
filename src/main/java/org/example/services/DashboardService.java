package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AchievementRequest;
import org.example.dto.response.*;
import org.example.entities.*;
import org.example.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final RelapseRepository relapseRepository;
    private final HabitRepository habitRepository;
    private final AchievementRepository achievementRepository;

    @Transactional(readOnly = true)
    public DashboardResponse getUserDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // 1. Carga de datos ordenados
        List<Relapse> relapses = relapseRepository.findByUser_IdOrderByRelapsedAtDesc(userId);
        List<Habit> habits = habitRepository.findByUser_Id(userId);
        List<Achievement> achievements = achievementRepository.findByUser_IdOrderByUnlockedAtDesc(userId);

        // 2. Cálculo de días limpios globales
        long daysCleanGlobal = 0;
        if (!relapses.isEmpty() && relapses.get(0).getRelapsedAt() != null) {
            daysCleanGlobal = ChronoUnit.DAYS.between(relapses.get(0).getRelapsedAt(), LocalDateTime.now());
        } else if (user.getCreatedAt() != null) {
            daysCleanGlobal = ChronoUnit.DAYS.between(user.getCreatedAt(), LocalDateTime.now());
        }

        // 3. Construcción del DTO
        return DashboardResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .daysClean(daysCleanGlobal)
                .totalAchievements(achievements.size())
                .activeHabitsCount(habits.size())
                .habits(mapHabits(habits))
                .recentAchievements(mapAchievements(achievements))
                .recentRelapses(mapRelapses(relapses, user))
                .build();
    }

    private List<HabitResponse> mapHabits(List<Habit> habits) {
        return habits.stream()
                .map(h -> {
                    long days = (h.getStartDate() != null)
                            ? ChronoUnit.DAYS.between(h.getStartDate(), LocalDateTime.now())
                            : 0;

                    return HabitResponse.builder()
                            .id(h.getId())
                            .name(h.getName())
                            .description(h.getDescription() != null ? h.getDescription() : "Sin descripción")
                            .category(h.getCategory() != null ? h.getCategory() : "General")
                            .currentStreak(h.getCurrentStreak() != null ? h.getCurrentStreak() : 0)
                            .daysClean(days)
                            .totalSaved(h.getTotalSaved() != null ? h.getTotalSaved() : 0.0)
                            .dailyExpense(h.getDailyExpense() != null ? h.getDailyExpense() : 0.0)
                            .startDate(h.getStartDate())
                            .currency(h.getCurrency() != null ? h.getCurrency() : "MXN")
                            .isActive(h.getIsActive() != null ? h.getIsActive() : true)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<RelapseResponse> mapRelapses(List<Relapse> relapses, User user) {
        return relapses.stream()
                .limit(5)
                .map(r -> RelapseResponse.builder()
                        .id(r.getId())
                        .reason(r.getReason() != null ? r.getReason() : "No especificada")
                        .emotion(r.getEmotion() != null ? r.getEmotion() : "No especificada")
                        .comment(r.getComment() != null ? r.getComment() : "")
                        .relapsedAt(r.getRelapsedAt())
                        .userId(user.getId())
                        .username(user.getFullName())
                        .build())
                .collect(Collectors.toList());
    }

    private List<AchievementResponse> mapAchievements(List<Achievement> achievements) {
        return achievements.stream()
                .limit(3)
                .map(a -> AchievementResponse.builder()
                        .id(a.getId())
                        .title(a.getTitle())
                        .description(a.getDescription())
                        .icon(a.getIcon())
                        .category(a.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Double calculateTotalSavings(Long userId) {
        return habitRepository.findByUser_Id(userId).stream()
                .mapToDouble(h -> h.getTotalSaved() != null ? h.getTotalSaved() : 0.0)
                .sum();
    }

    @Transactional
    public void processDailyCheckIn(Long userId) {
        List<Habit> habits = habitRepository.findByUser_Id(userId);
        if (habits.isEmpty()) {
            throw new RuntimeException("El usuario no tiene adicciones/hábitos registrados");
        }
        for (Habit habit : habits) {
            habit.setCurrentStreak((habit.getCurrentStreak() != null ? habit.getCurrentStreak() : 0) + 1);
            double savingsToday = habit.getDailyExpense() != null ? habit.getDailyExpense() : 0.0;
            habit.setTotalSaved((habit.getTotalSaved() != null ? habit.getTotalSaved() : 0.0) + savingsToday);
            habitRepository.save(habit);
        }
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getUserPerformance(Long userId) {
        List<Habit> habits = habitRepository.findByUser_Id(userId);
        double totalSaved = habits.stream().mapToDouble(h -> h.getTotalSaved() != null ? h.getTotalSaved() : 0.0).sum();
        int totalStreak = habits.stream().mapToInt(h -> h.getCurrentStreak() != null ? h.getCurrentStreak() : 0).sum();

        Map<String, Object> performance = new HashMap<>();
        performance.put("totalSavings", totalSaved);
        performance.put("accumulatedStreaks", totalStreak);
        performance.put("activeHabits", habits.size());
        performance.put("averageSavingsPerHabit", habits.isEmpty() ? 0 : totalSaved / habits.size());
        performance.put("motivationalMessage", totalSaved > 500 ? "¡Eres una máquina de ahorrar!" : "Cada día cuenta, sigue enfocado.");
        return performance;
    }

    public void handleSOS(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado"));
        // Aquí podrías disparar un correo o integración real en el futuro
        System.out.println("🚨 [ALERTA SOS ENVIADA] " + user.getFullName() + " ha activado el botón de pánico.");
    }
}