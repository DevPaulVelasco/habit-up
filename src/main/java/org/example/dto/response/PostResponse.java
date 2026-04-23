package org.example.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    private Long id;
    private String content;
    private Integer likes;
    private LocalDateTime createdAt;

    // --- INFO DEL AUTOR (Para que React sepa quién escribió) ---
    private Long userId;
    private String username;
    private String userAvatar; // Opcional: por si luego le pones fotos de perfil
}