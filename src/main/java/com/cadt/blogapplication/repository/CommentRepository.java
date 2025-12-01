package com.cadt.blogapplication.repository;

import com.cadt.blogapplication.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Custom Query Method
    // Spring Data JPA is smart. It sees "findByPostId" and writes the SQL:
    // SELECT * FROM comments WHERE post_id = ?
    List<Comment> findByPostId(long postId);
}
