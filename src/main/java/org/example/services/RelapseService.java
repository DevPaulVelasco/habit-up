package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.RelapseRequest;
import org.example.dto.response.RelapseResponse;
import org.example.entities.Habit;
import org.example.entities.Relapse;
import org.example.entities.User;
import org.example.repositories.HabitRepository;
import org.example.repositories.RelapseRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelapseService {

    private final RelapseRepository relapseRepository;
    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final NotificationService notificationService;

    /**
     * 1. REGISTRAR RECAÍDA
     * Guarda el tropiezo y reinicia todas las rachas de hábitos a cero.
     */
    @Transactional
    public RelapseResponse createRelapse(Long userId, RelapseRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // 1.1 Guardar el registro de la recaída
        Relapse relapse = Relapse.builder()
                .reason(request.getReason())
                .emotion(request.getEmotion())
                .comment(request.getComment())
                .relapsedAt(LocalDateTime.now())
                .user(user)
                .build();

        Relapse savedRelapse = relapseRepository.save(relapse);

        // 1.2 RESET DE RACHAS: Todos los hábitos activos vuelven a 0
        List<Habit> activeHabits = habitRepository.findByUser_Id(userId);
        for (Habit habit : activeHabits) {
            habit.setCurrentStreak(0);
            habit.setStartDate(LocalDateTime.now()); // El contador de "Días Limpios" reinicia hoy
            habitRepository.save(habit);
        }

        // 1.3 NOTIFICACIÓN: Avisar al Dashboard para actualizar la UI en tiempo real
        notificationService.sendDashboardUpdate(userId, "RELAPSE_RESET", "Se ha registrado una recaída. ¡Ánimo, puedes volver a empezar!");

        return mapToResponse(savedRelapse);
    }

    /**
     * 2. OBTENER HISTORIAL (Timeline)
     */
    @Transactional(readOnly = true)
    public List<RelapseResponse> getRelapseHistory(Long userId) {
        return relapseRepository.findByUser_IdOrderByRelapsedAtDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 3. DATOS PARA GRÁFICAS
     */
    @Transactional(readOnly = true)
    public List<Object[]> getRelapseStatsByReason(Long userId) {
        return relapseRepository.countRelapsesByReason(userId);
    }

    /**
     * 4. ELIMINAR REGISTRO
     */
    @Transactional
    public void deleteRelapse(Long id) {
        if (!relapseRepository.existsById(id)) {
            throw new org.example.exceptions.ResourceNotFoundException("El registro de recaída no existe");
        }
        relapseRepository.deleteById(id);
    }

    /**
     * MAPPER: Convierte Entidad a DTO de respuesta
     */
    private RelapseResponse mapToResponse(Relapse r) {
        return RelapseResponse.builder()
                .id(r.getId())
                .reason(r.getReason())
                .emotion(r.getEmotion())
                .comment(r.getComment())
                .relapsedAt(r.getRelapsedAt())
                .userId(r.getUser().getId())
                .username(r.getUser().getFullName())
                .build();
    }
}