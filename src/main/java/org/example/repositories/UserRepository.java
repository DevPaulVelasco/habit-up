package org.example.repositories;

import org.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su correo electrónico.
     * Crucial para el proceso de Login.
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica si un correo ya existe en la base de datos.
     * Crucial para el proceso de Registro (Evita duplicados).
     */
    Boolean existsByEmail(String email);

    /**
     * Busca un usuario cargando sus logros de forma optimizada (Eager).
     * Evita el LazyInitializationException en el Dashboard.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.achievements WHERE u.id = :id")
    Optional<User> findByIdWithAchievements(@Param("id") Long id);

    /**
     * Busca un usuario cargando sus hábitos para el perfil.
     */
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.habits WHERE u.id = :id")
    Optional<User> findByIdWithHabits(@Param("id") Long id);
}