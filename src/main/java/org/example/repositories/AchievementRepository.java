package org.example.repositories;

import org.example.entities.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {

    Optional<Achievement> findByRequiredDays(Integer requiredDays);

    // --- 1. VISTA DE PROGRESO ---
    List<Achievement> findByUser_Id(Long userId);

    List<Achievement> findByUser_IdOrderByUnlockedAtDesc(Long userId);

    List<Achievement> findByUser_IdAndCategory(Long userId, String category);

    // --- 2. LÓGICA DE NEGOCIO ---
    boolean existsByUser_IdAndTitle(Long userId, String title);

    // --- 3. CONTEO TOTAL ---
    long countByUser_Id(Long userId);

    // --- 4. BÚSQUEDA ESPECÍFICA (CORREGIDA) ---
    @Query("SELECT a FROM Achievement a WHERE a.user.id = :userId AND a.category = :category")
    List<Achievement> findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") String category);
}