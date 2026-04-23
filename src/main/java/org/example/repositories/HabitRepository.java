package org.example.repositories;

import org.example.entities.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    // --- 1. DASHBOARD (Vista 14) ---
    // Usamos User_Id para asegurar que busque por el ID de la entidad relacionada
    List<Habit> findByUser_IdAndIsActiveTrue(Long userId);

    // Seguridad: Buscar hábito asegurando que pertenezca al usuario logueado
    Optional<Habit> findByIdAndUser_Id(Long id, Long userId);


    // --- 2. PROGRESO (Vista 16) ---
    long countByUser_Id(Long userId);

    List<Habit> findByUser_IdAndCategory(Long userId, String category);


    // --- 3. IMPACTO FINANCIERO (Query Pro) ---
    // Si no hay hábitos, SUM devuelve null, así que usamos COALESCE para que devuelva 0.0
    @Query("SELECT COALESCE(SUM(h.dailyExpense), 0.0) FROM Habit h WHERE h.user.id = :userId AND h.isActive = true")
    Double sumDailyExpenseByUserId(@Param("userId") Long userId);


    // --- 4. BÚSQUEDA / SOS ---
    List<Habit> findByUser_IdAndNameContainingIgnoreCase(Long userId, String name);

    List<Habit> findByUserId(Long userId);

    List<Habit> findByUser_Id(Long userId);
}