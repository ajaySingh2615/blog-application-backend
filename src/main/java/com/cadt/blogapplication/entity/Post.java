package com.cadt.blogapplication.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)  // Allow longer text
    private String content;

    private String author;

    private LocalDateTime createdAt;

    // The Relationships (One Post -> Many Comments)
    // mappedBy = "Post": refers to the "post" field in Comment.java
    // CascadeType.ALL: if post is deleted, delete all comment too.
    // orphanRemoval = true: If a comment is removed from this list, delete it from DB
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();
}
