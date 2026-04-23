package org.example.controllers;

import jakarta.validation.Valid;
import org.example.dto.request.PostRequest;
import org.example.dto.response.PostResponse;
import org.example.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @PostMapping("")
    public ResponseEntity<PostResponse> createPost(
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails,
            @Valid @RequestBody PostRequest request) {
        return new ResponseEntity<>(postService.createPost(userDetails.getId(), request), HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> likePost(@PathVariable Long postId) {
        // Ahora devolvemos el post actualizado
        return ResponseEntity.ok(postService.giveLike(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails) {
        postService.deletePost(postId, userDetails.getId());
        return ResponseEntity.noContent().build(); // Devuelve 204 como en image_fba586.png
    }
}