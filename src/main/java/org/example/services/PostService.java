package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.PostRequest;
import org.example.dto.response.PostResponse;
import org.example.entities.Post;
import org.example.entities.User;
import org.example.repositories.PostRepository;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    /**
     * 1. CREAR POST
     * Guarda el post y notifica vía WebSocket.
     */
    @Transactional
    public PostResponse createPost(Long userId, PostRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Usuario no encontrado"));

        Post post = Post.builder()
                .content(request.getContent())
                .user(user)
                .likes(0)
                .createdAt(LocalDateTime.now()) // Aseguramos la fecha
                .build();

        Post savedPost = postRepository.save(post);

        // 🔥 AVISO WEBSOCKET
        notificationService.sendDashboardUpdate(userId, "NEW_POST", "¡Has compartido un nuevo mensaje!");

        return mapToResponse(savedPost);
    }

    /**
     * 2. OBTENER EL MURO
     */
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 3. DAR LIKE (Optimizado + Notificación)
     */
    @Transactional
    public PostResponse giveLike(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new org.example.exceptions.ResourceNotFoundException("El post no existe");
        }

        // Incrementamos directo en DB
        postRepository.incrementLikes(postId);

        // Recargamos el post para tener el contador real tras el incremento
        Post post = postRepository.findById(postId).get();

        // 🔥 AVISO WEBSOCKET al dueño del post
        notificationService.sendDashboardUpdate(post.getUser().getId(), "POST_LIKE", "¡Alguien le dio like a tu publicación!");

        return mapToResponse(post);
    }

    /**
     * 4. ELIMINAR POST
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new org.example.exceptions.ResourceNotFoundException("Post no encontrado"));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("No tienes permiso para borrar este post");
        }

        postRepository.delete(post);
    }

    /**
     * MAPPER INTERNO
     */
    private PostResponse mapToResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .likes(post.getLikes() != null ? post.getLikes() : 0)
                .createdAt(post.getCreatedAt())
                .userId(post.getUser().getId())
                .username(post.getUser().getFullName())
                .build();
    }
}