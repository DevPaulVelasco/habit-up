package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.entities.Comment;
import org.example.entities.Post;
import org.example.entities.User;
import org.example.repositories.CommentRepository;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public void addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Post no encontrado"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado"));

        Comment comment = Comment.builder()
                .content(content)
                .post(post)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        // 🔥 AVISO WEBSOCKET: Avisamos al dueño del post que tiene un nuevo comentario
        if (!post.getUser().getId().equals(userId)) { // No avisar si yo mismo comento mi post
            notificationService.sendDashboardUpdate(
                    post.getUser().getId(),
                    "NEW_COMMENT",
                    user.getFullName() + " ha comentado tu publicación."
            );
        }
    }

    @Transactional(readOnly = true)
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }
}