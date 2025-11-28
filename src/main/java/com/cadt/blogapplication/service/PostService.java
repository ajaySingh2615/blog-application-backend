package com.cadt.blogapplication.service;

import com.cadt.blogapplication.entity.Post;
import com.cadt.blogapplication.exception.ResourceNotFoundException;
import com.cadt.blogapplication.payload.PostDto;
import com.cadt.blogapplication.payload.PostResponse;
import com.cadt.blogapplication.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // 1. Create Sort object (Senior Logic: Handle Ascending/Descending)
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // 2. Create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // 3. Get the Page from DB (Not a List, but a 'Page')
        Page<Post> postsPage = postRepository.findAll(pageable);

        // 4. Get the content (List of entities) from the Page
        List<Post> listOfPosts = postsPage.getContent();

        // Convert Entities to DTOs
        List<PostDto> content = listOfPosts.stream().map(this::mapToDTO).toList();

        // Create the response object and fill it
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(postsPage.getNumber());
        postResponse.setPageSize(postsPage.getSize());
        postResponse.setTotalElements(postsPage.getTotalElements()); // Crucial for frontend
        postResponse.setTotalPages(postsPage.getTotalPages());
        postResponse.setLast(postsPage.isLast()); // Should I disable the next button?
        return postResponse;
    }

    // Feature 3: Get a single post by ID
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                // If found, good. If not, THROW exception.
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    // Feature 4: Delete a post
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("post", "id", id));

        postRepository.delete(post);
    }

    // Feature 5: Update a post
    public PostDto updatePost(Long id, PostDto postDto) {
        // 1. Find the existing post
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);

        return mapToDTO(updatedPost);
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
