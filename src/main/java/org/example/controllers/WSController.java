package org.example.controllers;

import org.example.dto.response.UserResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * El cerebro del tiempo real.
 * Se comunica con las vistas 15 (Comunidad) y el Botón SOS.
 */
@Controller
public class WSController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 1. CANAL DE EMERGENCIA (SOS)
     * Alex envía a: /app/sos
     * Los suscriptores escuchan en: /topic/alerts
     */
    @MessageMapping("/sos")
    @SendTo("/topic/alerts")
    public Map<String, Object> handleSOS(@Payload UserResponse user) {
        Map<String, Object> response = new HashMap<>();
        response.put("action", "EMERGENCY_ALARM");
        response.put("userDetails.getId()", user.getId());
        response.put("userName", user.getFullName());
        response.put("message", "¡ATENCIÓN! " + user.getFullName() + " ha activado el botón SOS.");
        response.put("timestamp", LocalDateTime.now().toString());
        response.put("severity", "CRITICAL");

        return response;
    }

    /**
     * 2. NOTIFICACIÓN DE PROGRESO (Vista 16)
     * Cuando Alex completa una racha, se le avisa a sus amigos.
     */
    @MessageMapping("/habit-complete")
    @SendTo("/topic/community")
    public Map<String, Object> handleHabitUpdate(@Payload Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("action", "HABIT_MILESTONE");
        response.put("message", data.get("userName") + " completó su hábito: " + data.get("habitName"));
        response.put("streak", data.get("currentStreak"));
        response.put("timestamp", LocalDateTime.now().toString());

        return response;
    }

    /**
     * 3. MÉTODO PARA ENVIAR DESDE OTROS SERVICES
     * Este no lo llama el cliente, lo usamos nosotros desde Java (ej: AuthService o RelapseService)
     */
    public void sendSystemNotification(String destination, Object message) {
        messagingTemplate.convertAndSend(destination, message);
    }
}