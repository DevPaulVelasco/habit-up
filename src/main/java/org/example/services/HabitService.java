package org.example.services;

import org.example.dto.request.HabitRequest;
import org.example.dto.request.RelapseRequest;
import org.example.dto.response.HabitResponse;
import org.example.entities.Habit;
import org.example.entities.User;
import org.example.repositories.HabitRepository;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.transaction.annotation.Transactional;

import org.example.exceptions.ResourceNotFoundException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HabitService {

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public HabitResponse createHabit(Long userId, HabitRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        Habit habit = Habit.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                // Si viene fecha en el request se usa, si no, se pone la de ahora mismo
                .startDate(request.getStartDate() != null ? request.getStartDate() : LocalDateTime.now())
                .dailyExpense(request.getDailyExpense() != null ? request.getDailyExpense() : 0.0)
                .currency(request.getCurrency() != null ? request.getCurrency() : "MXN")
                .user(user)
                .isActive(true)
                .currentStreak(0)
                .totalSaved(0.0)
                .build();

        return mapToResponse(habitRepository.save(habit));
    }

    @Transactional(readOnly = true)
    public HabitResponse getHabitById(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado con ID: " + habitId));

        // Si el hábito está desactivado, lanzamos error para que no se pueda consultar individualmente
        if (!habit.getIsActive()) {
            throw new ResourceNotFoundException("Este hábito ha sido eliminado.");
        }

        return mapToResponse(habit);
    }

    @Transactional(readOnly = true)
    public List<HabitResponse> getActiveHabits(Long userId) {
        // Usamos el método que filtra por IsActiveTrue en la base de datos
        return habitRepository.findByUser_IdAndIsActiveTrue(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public HabitResponse updateHabit(Long habitId, HabitRequest request) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado"));

        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setCategory(request.getCategory());
        habit.setDailyExpense(request.getDailyExpense());
        if (request.getCurrency() != null) habit.setCurrency(request.getCurrency());

        return mapToResponse(habitRepository.save(habit));
    }

    @Transactional
    public HabitResponse processRelapse(Long habitId, RelapseRequest request) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado"));

        habit.setStartDate(LocalDateTime.now());
        habit.setCurrentStreak(0);

        return mapToResponse(habitRepository.save(habit));
    }

    @Transactional
    public void deleteHabit(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new ResourceNotFoundException("Hábito no encontrado"));

        // BORRADO LÓGICO
        habit.setIsActive(false);
        habitRepository.save(habit);
    }

    private HabitResponse mapToResponse(Habit habit) {
        long daysClean = 0;
        if (habit.getStartDate() != null) {
            // Usamos ChronoUnit o Duration para calcular los días de diferencia
            daysClean = Duration.between(habit.getStartDate(), LocalDateTime.now()).toDays();
        }

        return HabitResponse.builder()
                .id(habit.getId())
                .name(habit.getName())
                .description(habit.getDescription())
                .category(habit.getCategory())
                .startDate(habit.getStartDate())
                .dailyExpense(habit.getDailyExpense())
                .currency(habit.getCurrency())
                .isActive(habit.getIsActive())
                .currentStreak(habit.getCurrentStreak() != null ? habit.getCurrentStreak() : 0)
                .daysClean(Math.max(0, daysClean))
                .totalSaved(habit.getTotalSaved() != null ? habit.getTotalSaved() : 0.0)
                .build();
    }
}