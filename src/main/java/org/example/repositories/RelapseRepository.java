package org.example.repositories;

import org.example.entities.Relapse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelapseRepository extends JpaRepository<Relapse, Long> {

    /**
     * 1. HISTORIAL DE RECAÍDAS
     * Muestra el historial completo ordenado por fecha descendente.
     */
    List<Relapse> findByUser_IdOrderByRelapsedAtDesc(Long userId);

    /**
     * 2. ANALÍTICA DE CAUSAS (Gráficas de Razones)
     * Retorna: [ ["Presión Social", 5], ["Estrés", 3] ]
     */
    @Query("SELECT r.reason, COUNT(r) FROM Relapse r WHERE r.user.id = :userId GROUP BY r.reason ORDER BY COUNT(r) DESC")
    List<Object[]> countRelapsesByReason(@Param("userId") Long userId);

    /**
     * 3. ANALÍTICA DE EMOCIONES (Gráficas de Sentimientos)
     * Retorna: [ ["Frustración", 4], ["Tristeza", 2] ]
     */
    @Query("SELECT r.emotion, COUNT(r) FROM Relapse r WHERE r.user.id = :userId GROUP BY r.emotion")
    List<Object[]> countRelapsesByEmotion(@Param("userId") Long userId);

    /**
     * 4. ÚLTIMO TROPIEZO
     * Clave para calcular el tiempo de sobriedad actual.
     */
    Relapse findFirstByUser_IdOrderByRelapsedAtDesc(Long userId);

    /**
     * 5. CONTEO TOTAL
     * Para estadísticas rápidas en el perfil.
     */
    long countByUser_Id(Long userId);
}