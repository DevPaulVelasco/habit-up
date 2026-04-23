package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Este DTO es el que enviamos al Frontend después de que Alex
 * se registra o inicia sesión con éxito.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String fullName; // Coincide con el campo de tu Entidad User

    private String email;

    // Aquí puedes agregar más campos que el Frontend necesite mostrar de inicio
    // ej: private String profilePicture;
}