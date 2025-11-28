package com.cadt.blogapplication.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data  // Generates getters/setters
public class PostDto {
    private Long id;

    // Rule 1: Title cannot be null or empty
    // Rule 2: Title must be at least 2 characters
    @NotEmpty(message = "Title should not be null or empty")
    @Size(min = 2, message = "Post title should have at least 2 characters")
    private String title;

    // Rule 3: Content must be at least 10 characters
    @NotEmpty(message = "Content should not be null or empty")
    @Size(min = 10, message = "Post content should have at least 10 characters")
    private String content;

    @NotEmpty
    private String author;

    // Note: We might NOT want to expose 'createdAt' to the user when creating a post,
    // but let's keep it here for the response.
}
