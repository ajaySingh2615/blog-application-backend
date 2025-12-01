package com.cadt.blogapplication.service;

import java.util.List;
import java.util.stream.Collectors;

import com.cadt.blogapplication.entity.Comment;
import com.cadt.blogapplication.repository.CommentRepository;
import org.springframework.stereotype.Service;

import com.cadt.blogapplication.entity.Post;
import com.cadt.blogapplication.exception.ResourceNotFoundException;
import com.cadt.blogapplication.payload.CommentDto;
import com.cadt.blogapplication.repository.PostRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // Feature 1: Create comment for a specific Post
    public CommentDto createComment(long postId, CommentDto commentDto) {
        // 1. Retrieve Post entity by id (Throw error if not found)
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // 2. Convert DTO to Entity
        Comment comment = mapTOEntity(commentDto);

        // 3. Link comment to Post (Crucial step)
        comment.setPost(post);

        // 4. Save Comment to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDTO(newComment);
    }

    // Feature 2: Get All Comments by Post ID
    public List<CommentDto> getCommentsByPostId(long postId) {
        // 1. Check if post exists
        // (We don't strictly need the post object, just checking existence is safer)
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post", "id", postId);
        }

        // 2. Fetch comments from DB using the Custom Repository method
        List<Comment> comments = commentRepository.findByPostId(postId);

        // 3. Convert List<Entity> to List<DTO>
        return comments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    // Mapper
    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapTOEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
