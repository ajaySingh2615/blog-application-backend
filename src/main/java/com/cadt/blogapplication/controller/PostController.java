package com.cadt.blogapplication.controller;

import com.cadt.blogapplication.entity.Post;
import com.cadt.blogapplication.payload.PostDto;
import com.cadt.blogapplication.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // @Controller and @ResponseBody
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
            @RequestBody PostDto postDto
    ) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    // 2. GET API: Get all posts
    // URL: GET http://localhost:8080/api/posts
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 3. GET API: Get one post by ID
    // URL: GET http://localhost:8080/api/posts/1
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(
            @PathVariable Long id
    ) {
        Post post = postService.getPostById(id);

        if (post != null) {
            return new ResponseEntity<>(post, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 4. DELETE API: Delete a post
    // URL: DELETE http://localhost:8080/api/posts/1
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id
    ) {
        try {
            postService.deletePost(id);
            return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 5. PUT API: Update an existing post
    // URL: PUT http://localhost:8080/api/posts/1
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable Long id, @RequestBody Post postDetails
    ) {
        Post updatedPost = postService.updatePost(id, postDetails);

        if (updatedPost != null) {
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
