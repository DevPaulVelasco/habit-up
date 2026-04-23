package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.services.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> createComment(
            @PathVariable Long postId,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails,
            @RequestBody Map<String, String> body) {

        commentService.addComment(postId, userDetails.getId(), body.get("content"));
        return ResponseEntity.ok(Map.of("message", "Comentario publicado"));
    }
}