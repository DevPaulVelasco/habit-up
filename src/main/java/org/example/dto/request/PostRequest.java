package org.example.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostRequest {

    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(max = 500, message = "El post es muy largo (máximo 500 caracteres)")
    private String content;
}