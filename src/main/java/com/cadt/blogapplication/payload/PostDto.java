package com.cadt.blogapplication.payload;

import lombok.Data;

@Data  // Generates getters/setters
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    // Note: We might NOT want to expose 'createdAt' to the user when creating a post,
    // but let's keep it here for the response.
}
