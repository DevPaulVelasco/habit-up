package org.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Email(message = "Email inválido")
    @NotBlank(message = "El email es requerido")
    private String email;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}