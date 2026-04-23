package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String fullName;
    private String email;
    private String avatarUrl;
    private String bio;
    private String location;
    private String phoneNumber; // Ahora es una variable real
    private LocalDateTime createdAt;
    private Boolean isPrivate;
    private Boolean notificationsEnabled; // Ahora es una variable real
    private String token;
}