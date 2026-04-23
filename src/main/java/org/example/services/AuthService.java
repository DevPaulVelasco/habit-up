package org.example.services;

import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.response.UserResponse;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo ya está registrado");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword()) // En el futuro usaremos BCrypt
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser); // Retorna UserResponse, no String
    }

    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return mapToResponse(user); // Retorna UserResponse, no la Entidad
    }

    // Método helper para convertir Entidad -> Response
    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .build();
    }
}