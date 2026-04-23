package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.DashboardResponse;
import org.example.services.DashboardService;
import org.example.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor // 🔥 Esto inyecta automáticamente lo que sea 'private final'
@CrossOrigin(origins = "*")
public class DashboardController {

    // 1. Definimos los servicios como 'private final'
    private final DashboardService dashboardService;
    private final NotificationService notificationService;

    /**
     * 1. VISTA GENERAL
     */
    @GetMapping("")
    public ResponseEntity<DashboardResponse> getDashboard(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getUserDashboard(userDetails.getId()));
    }

    /**
     * 2. CHECK-IN DIARIO
     */
    @PostMapping("/check-in")
    public ResponseEntity<?> dailyCheckIn(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        dashboardService.processDailyCheckIn(userDetails.getId());

        // Notificamos por WebSocket que el dashboard cambió (opcional pero pro)
        notificationService.sendDashboardUpdate(userDetails.getId(), "CHECK_IN", "¡Día completado!");

        return ResponseEntity.ok(Map.of("message", "¡Felicidades! Un día más a la cuenta."));
    }

    /**
     * 3. BOTÓN DE PÁNICO / SOS
     */
    @PostMapping("/sos")
    public ResponseEntity<?> triggerSOS(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        dashboardService.handleSOS(userDetails.getId());

        // 🔥 Ahora sí, notificationService ya no saldrá en rojo
        notificationService.sendSOSAlert(userDetails.getId(), "Usuario " + userDetails.getId());

        return ResponseEntity.ok(Map.of("message", "Alerta enviada a tus contactos de apoyo. ¡Respira, no estás solo!"));
    }

    /**
     * 4. ESTADÍSTICAS DE AHORRO
     */
    @GetMapping("/savings")
    public ResponseEntity<?> getSavings(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(Map.of("totalSaved", dashboardService.calculateTotalSavings(userDetails.getId())));
    }

    /**
     * 5. RENDIMIENTO / PERFORMANCE
     */
    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> getUserPerformance(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(dashboardService.getUserPerformance(userDetails.getId()));
    }
}