package com.cadt.blogapplication.service;

import com.cadt.blogapplication.entity.Post;
import com.cadt.blogapplication.payload.PostDto;
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
    public PostDto createPost(PostDto postDto) {
        // 1. Convert DTO to Entity
        Post post = mapToEntity(postDto);

        // 2. Business Logic (Time)
        post.setCreatedAt(LocalDateTime.now());

        // 3. Save Entity to DB
        Post newPost = postRepository.save(post);

        // 4. Convert Entity back to DTO for response
        return mapToDTO(newPost);
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

    // Convert Entity to DTO
    private PostDto mapToDTO(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setAuthor(post.getAuthor());
        return postDto;
    }

    // Convert DTO to Entity
    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setAuthor(postDto.getAuthor());
        return post;
    }
}
