package org.example.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.request.UserRegisterRequest;
import org.example.dto.response.UserDto;
import org.example.dto.response.UserProfileDTO;
import org.example.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // Esto inyecta el UserService de forma segura y quita el error de compilación
public class UserController {

    private final UserService userService; // Marcado como final para inyección obligatoria

    /**
     * REGISTRO DE USUARIO (Vista 12)
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest request) {
        try {
            UserDto response = userService.registerUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * LOGIN DE USUARIO (Vista 13)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            UserDto response = userService.login(email, password);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * OBTENER PERFIL DETALLADO (Vista 20)
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    /**
     * ACTUALIZAR PERFIL (Vista 21)
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateProfile(@PathVariable Long id, @RequestBody UserDto updateData) {
        return ResponseEntity.ok(userService.updateUser(id, updateData));
    }

    /**
     * ELIMINAR CUENTA (Vista 22)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        // Esta es la línea que fallaba en tu imagen.
        // Con el Rebuild y el Service actualizado, el error desaparecerá.
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}