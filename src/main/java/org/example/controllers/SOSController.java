package org.example.controllers;

import org.example.services.SOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador de Emergencia (Botón SOS).
 * Se comunica con el SOSService para disparar alertas vía WebSockets.
 */
@RestController
@RequestMapping("/api/sos")
@CrossOrigin(origins = "*")
public class SOSController {

    @Autowired
    private SOSService sosService;

    /**
     * DISPARAR ALERTA SOS
     * POST /api/sos/trigger
     */
    @PostMapping("/trigger")
    public ResponseEntity<Map<String, Object>> triggerSOS(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        // Llamamos al servicio que maneja la lógica y las notificaciones WS
        return ResponseEntity.ok(sosService.triggerSOS(userDetails.getId()));
    }

    /**
     * CANCELAR ALERTA
     * POST /api/sos/cancel
     */
    @PostMapping("/cancel")
    public ResponseEntity<String> cancelSOS(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        // Notificamos que la crisis pasó a través del servicio
        return ResponseEntity.ok(sosService.cancelSOS(userDetails.getId()));
    }
}