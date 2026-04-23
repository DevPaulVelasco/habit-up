package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.request.RelapseRequest;
import org.example.dto.response.RelapseResponse;
import org.example.services.RelapseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relapses")
@CrossOrigin(origins = "*") // Para que Alex se conecte desde cualquier puerto de React
public class RelapseController {

    @Autowired
    private RelapseService relapseService;

    /**
     * 1. POST /api/relapses
     * Registrar un nuevo tropiezo (Vista de Emergencia/Botón Pánico).
     */
    @PostMapping("")
    public ResponseEntity<RelapseResponse> createRelapse(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails,
            @Valid @RequestBody RelapseRequest request) {

        RelapseResponse response = relapseService.createRelapse(userDetails.getId(), request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 2. GET /api/relapses
     * Obtener el historial completo (Vista 16 - Timeline).
     */
    @GetMapping("")
    public ResponseEntity<List<RelapseResponse>> getHistory(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(relapseService.getRelapseHistory(userDetails.getId()));
    }

    /**
     * 3. GET /api/relapses/stats
     * Obtener datos agrupados para Gráficas (Pie/Bar Charts).
     * Devuelve: [ ["Ansiedad", 5], ["Presión Social", 2] ]
     */
    @GetMapping("/stats")
    public ResponseEntity<List<Object[]>> getStats(@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        return ResponseEntity.ok(relapseService.getRelapseStatsByReason(userDetails.getId()));
    }

    /**
     * 4. DELETE /api/relapses/{id}
     * Por si Alex quiere borrar un registro erróneo.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelapse(@PathVariable Long id) {
        relapseService.deleteRelapse(id);
        return ResponseEntity.noContent().build();
    }
}