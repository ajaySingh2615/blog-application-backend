package com.cadt.blogapplication.repository;

import com.cadt.blogapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // for Login: find user by email
    Optional<User> findByEmail(String email);

    // for Login: Find user by username or email (flexible login)
    Optional<User> findByUsernameOrEmail(String username, String email);

    // For Registration: Check if user exists
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

}
