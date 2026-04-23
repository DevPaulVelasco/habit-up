package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.AchievementRequest;
import org.example.dto.response.AchievementResponse;
import org.example.entities.Achievement;
import org.example.entities.User;
import org.example.repositories.AchievementRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AchievementResponse> getAchievementsByUserId(Long userId) {
        return achievementRepository.findByUser_Id(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AchievementResponse> getAchievementsByCategory(Long userId, String category) {
        return achievementRepository.findByUser_IdAndCategory(userId, category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * CORRECCIÓN: Ahora recibe el AchievementRequest completo
     * para evitar el Error 500 por mismatch de parámetros.
     */
    @Transactional
    public AchievementResponse giveAchievement(Long userId, AchievementRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // Verificamos usando el título que viene en el JSON
        if (achievementRepository.existsByUser_IdAndTitle(userId, request.getTitle())) {
            throw new RuntimeException("El usuario ya tiene este logro: " + request.getTitle());
        }

        // Usamos el Builder con todos los campos del Request para que no haya NULLS en la DB
        Achievement newAchievement = Achievement.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .icon(request.getIcon())
                .category(request.getCategory())
                .requiredDays(1) // Valor por defecto
                .unlockedAt(LocalDateTime.now())
                .user(user)
                .build();

        return mapToResponse(achievementRepository.save(newAchievement));
    }

    private AchievementResponse mapToResponse(Achievement a) {
        // Aseguramos que el builder no falle si algún campo viene nulo de la DB
        return AchievementResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .description(a.getDescription())
                .icon(a.getIcon())
                .category(a.getCategory())
                .build();
    }
}