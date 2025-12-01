package com.cadt.blogapplication.controller;

import com.cadt.blogapplication.payload.CommentDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cadt.blogapplication.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/") // Base path
@CrossOrigin(origins = "http://localhost:5173") // Allow React
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 1. Create Comment API
    // POST http://localhost:8080/api/posts/1/comments
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable(value = "postId") long postId,
            @Valid @RequestBody CommentDto commentDto
    ) {
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    // 2. Get Comments API
    // GET http://localhost:8080/api/posts/1/comments
    public List<CommentDto> getCommentByPostId(@PathVariable(value = "postId") Long postId) {
        return commentService.getCommentsByPostId(postId);
    }
}
