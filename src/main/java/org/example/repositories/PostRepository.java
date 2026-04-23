package org.example.repositories;

import org.example.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // --- 1. EL MURO (Vista 15) ---
    // Trae todos los posts ordenados por fecha de creación (del más nuevo al más viejo)
    List<Post> findAllByOrderByCreatedAtDesc();

    // --- 2. PERFIL DEL USUARIO ---
    // Si Alex entra a su perfil, solo quiere ver SUS publicaciones
    List<Post> findByUser_IdOrderByCreatedAtDesc(Long userId);

    // --- 3. LÓGICA DE LIKES (Optimización Pro) ---
    // En lugar de traer el objeto, sumarle 1 y volverlo a guardar (que es lento),
    // hacemos un UPDATE directo en la base de datos. ¡Mucho más rápido!
    @Modifying
    @Query("UPDATE Post p SET p.likes = p.likes + 1 WHERE p.id = :postId")
    void incrementLikes(@Param("postId") Long postId);

    // --- 4. BÚSQUEDA ---
    // Por si quieres buscar posts que hablen de "Recaída" o "Logro"
    List<Post> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);
}