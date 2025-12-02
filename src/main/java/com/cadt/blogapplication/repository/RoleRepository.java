package com.cadt.blogapplication.repository;

import com.cadt.blogapplication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // find role by name (e.g., "ROLE ADMIN")
    Optional<Role> findByName(String name);
}
