package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.AchievementRequest;
import org.example.dto.response.AchievementResponse;
import org.example.services.AchievementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // Obtener todos los logros de un usuario
    @GetMapping("")
    public ResponseEntity<List<AchievementResponse>> getMyAchievements(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(achievementService.getAchievementsByUserId(userDetails.getId()));
    }

    // Obtener logros por categoría
    @GetMapping("/category/{category}")
    public ResponseEntity<List<AchievementResponse>> getByCategory(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails,
            @PathVariable String category) {
        return ResponseEntity.ok(achievementService.getAchievementsByCategory(userDetails.getId(), category));
    }

    /**
     * ARREGLO: Cambiamos @RequestParam por @RequestBody AchievementRequest.
     * Ahora el controlador recibe el JSON completo de Insomnia.
     */
    @PostMapping("/unlock")
    public ResponseEntity<AchievementResponse> unlockManual(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails,
            @Valid @RequestBody AchievementRequest request) { // 👈 Recibe el JSON completo
        return ResponseEntity.ok(achievementService.giveAchievement(userDetails.getId(), request));
    }
}