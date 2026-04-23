package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.UserRegisterRequest;
import org.example.dto.response.UserDto;
import org.example.dto.response.UserProfileDTO;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.example.exceptions.ResourceNotFoundException;
import org.example.exceptions.UnauthorizedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.example.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public UserDto registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Error: El correo " + request.getEmail() + " ya está registrado.");
        }

        User newUser = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio("¡Hola! Estoy usando Habit Up.")
                .avatarUrl("https://ui-avatars.com/api/?name=" + request.getFullName().replace(" ", "+"))
                .location("No especificada")
                .isPrivate(false)
                .notificationsEnabled(true)
                .build();

        User savedUser = userRepository.save(newUser);
        String token = jwtTokenProvider.generateToken(savedUser.getEmail());
        
        return mapToDTO(savedUser, token);
    }

    private UserDto mapToDTO(User user, String token) {
        return UserDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .bio(user.getBio() != null ? user.getBio() : "Sin biografía")
                .avatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "")
                .location(user.getLocation() != null ? user.getLocation() : "No definida")
                .phoneNumber(user.getPhoneNumber() != null ? user.getPhoneNumber() : "Sin teléfono")
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now())
                .isPrivate(user.getIsPrivate() != null ? user.getIsPrivate() : false)
                .notificationsEnabled(user.getNotificationsEnabled() != null ? user.getNotificationsEnabled() : true)
                .token(token)
                .build();
    }

    @Transactional(readOnly = true)
    public UserDto login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }
        
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return mapToDTO(user, token);
    }

    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + userId));

        return UserProfileDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .bio(user.getBio())
                .avatarUrl(user.getAvatarUrl())
                .location(user.getLocation())
                .isPrivate(user.getIsPrivate())
                .postsCount((long) user.getPosts().size())
                .achievementsCount((long) user.getAchievements().size())
                .habitsCount((long) user.getHabits().size())
                .recentAchievementIcons(user.getAchievements().stream()
                        .limit(3)
                        .map(a -> a.getIcon())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto updateData) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        if (updateData.getFullName() != null) user.setFullName(updateData.getFullName());
        if (updateData.getBio() != null) user.setBio(updateData.getBio());
        if (updateData.getAvatarUrl() != null) user.setAvatarUrl(updateData.getAvatarUrl());
        if (updateData.getLocation() != null) user.setLocation(updateData.getLocation());
        if (updateData.getPhoneNumber() != null) user.setPhoneNumber(updateData.getPhoneNumber());
        if (updateData.getIsPrivate() != null) user.setIsPrivate(updateData.getIsPrivate());
        if (updateData.getNotificationsEnabled() != null)
            user.setNotificationsEnabled(updateData.getNotificationsEnabled());

        return mapToDTO(userRepository.save(user), null);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}