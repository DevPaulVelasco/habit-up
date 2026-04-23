package org.example.repositories;

import org.example.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Para ver todos los comentarios de un post específico
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);
}