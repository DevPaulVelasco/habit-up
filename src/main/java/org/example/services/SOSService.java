package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SOSService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public Map<String, Object> triggerSOS(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        // Lanzamos el aviso por WebSocket a todos los que escuchan el canal SOS
        notificationService.sendSOSAlert(userId, user.getFullName());

        Map<String, Object> sosData = new HashMap<>();
        sosData.put("status", "EMERGENCY_ACTIVE");
        sosData.put("message", "¡ALERTA ENVIADA! " + user.getFullName() + " necesita apoyo.");
        sosData.put("timestamp", LocalDateTime.now());
        sosData.put("userLocation", user.getLocation() != null ? user.getLocation() : "No compartida");

        return sosData;
    }

    @Transactional(readOnly = true)
    public String cancelSOS(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado"));

        notificationService.sendDashboardUpdate(userId, "SOS_CANCELLED", "La crisis ha pasado.");

        return "Alerta SOS cancelada. ¡Qué bueno que estás bien, " + user.getFullName() + "!";
    }
}