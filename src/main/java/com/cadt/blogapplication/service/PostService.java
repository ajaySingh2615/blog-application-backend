package com.cadt.blogapplication.service;

import com.cadt.blogapplication.entity.Post;
import com.cadt.blogapplication.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Feature 1: Create a new Post
    public Post createPost(Post post) {
        // Business logic: Set the time automatically before saving
        post.setCreatedAt(LocalDateTime.now());

        // call the repository to save to DB
        return postRepository.save(post);
    }

    // Feature 2: Get all Posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Feature 3: Get a single post by ID
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    // Feature 4: Delete a post
    public void deletePost(Long id) {
        // Check if it exists before trying to delete (Good practice)
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            // In a real app, we would throw a custom exception here
            throw new RuntimeException("Post not found with id: " + id);
        }
    }

    // Feature 5: Update a post
    public Post updatePost(Long id, Post postDetails) {
        // 1. Find the existing post
        Post existingPost = postRepository.findById(id).orElse(null);

        if (existingPost != null) {
            // 2. Update the fields with new data
            existingPost.setTitle(postDetails.getTitle());
            existingPost.setContent(postDetails.getContent());

            // 3. Save it back to the DB
            // JPA is smart: If ID exists, it does UPDATE. If ID is null, it does INSERT.
            return postRepository.save(existingPost);
        }
        return null;  // Or throw exception
    }


}
