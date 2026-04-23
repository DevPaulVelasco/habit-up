package org.example.services;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    // El SimpMessagingTemplate es la herramienta que Spring usa para
    // enviar mensajes a través del Broker de WebSockets.
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Notifica actualizaciones generales del Dashboard (Recaídas, Check-ins, etc.)
     * El Front se suscribirá a: /topic/updates/{userId}
     */
    public void sendDashboardUpdate(Long userId, String action, String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", action);      // Ej: "REFRESH", "RESET_STREAK"
        payload.put("message", message);    // Ej: "Se ha registrado una recaída"
        payload.put("userId", userId);
        payload.put("timestamp", LocalDateTime.now().toString());

        messagingTemplate.convertAndSend("/topic/updates/" + userId, payload);
    }

    /**
     * Alerta SOS de alta prioridad.
     * El Front se suscribirá a: /topic/sos/{userId}
     */
    public void sendSOSAlert(Long userId, String username) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "CRITICAL_SOS");
        payload.put("username", username);
        payload.put("alert", "🚨 ¡ATENCIÓN! " + username + " ha activado el botón de auxilio.");
        payload.put("timestamp", LocalDateTime.now().toString());

        messagingTemplate.convertAndSend("/topic/sos/" + userId, payload);
    }

    /**
     * Notificación de Logros desbloqueados.
     */
    public void sendAchievementNotification(Long userId, String achievementTitle) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("type", "ACHIEVEMENT_UNLOCKED");
        payload.put("title", achievementTitle);
        payload.put("message", "¡Felicidades! Has ganado una nueva medalla.");

        messagingTemplate.convertAndSend("/topic/updates/" + userId, payload);
    }
}