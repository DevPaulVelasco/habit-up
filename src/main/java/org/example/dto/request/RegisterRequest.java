package org.example.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    private String fullName;

    @Email(message = "El formato del email no es válido")
    @NotBlank(message = "El email no puede estar vacío")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}