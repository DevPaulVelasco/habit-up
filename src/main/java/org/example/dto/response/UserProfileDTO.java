package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {

    // --- INFORMACIÓN BÁSICA ---
    private Long id;
    private String fullName;
    private String avatarUrl;
    private String bio;
    private String location;

    // --- CONTADORES (Sincronizados con el Service) ---
    private Long postsCount;
    private Long achievementsCount;
    private Long habitsCount; // Este es el que faltaba y daba error

    // --- SOCIAL (Opcionales para el futuro) ---
    private Long followersCount;
    private Long followingCount;

    // --- ESTADOS ---
    private Boolean isFollowing;
    private Boolean isPrivate;

    // --- VITRINA ---
    private List<String> recentAchievementIcons;
}