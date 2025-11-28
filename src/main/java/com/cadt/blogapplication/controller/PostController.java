package com.cadt.blogapplication.controller;

import com.cadt.blogapplication.payload.PostDto;
import com.cadt.blogapplication.payload.PostResponse;
import com.cadt.blogapplication.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController // @Controller and @ResponseBody
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 1. Post API: Create a Post API
    // URL: POST http://localhost:8080/api/posts
    @PostMapping
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // 2. GET API: Get all posts
    // URL: GET http://localhost:8080/api/posts
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
        return new ResponseEntity<>(
                postService.getAllPosts(pageNo, pageSize, sortBy, sortDir), HttpStatus.OK);
    }

    // 3. GET API: Get one post by ID
    // URL: GET http://localhost:8080/api/posts/1
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable Long id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.OK);
    }

    // 4. DELETE API: Delete a post
    // URL: DELETE http://localhost:8080/api/posts/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id) {
        postService.deletePost(id);

        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }

    // 5. PUT API: Update an existing post
    // URL: PUT http://localhost:8080/api/posts/1
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @Valid @PathVariable Long id, @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.updatePost(id, postDto), HttpStatus.OK);
    }
}
