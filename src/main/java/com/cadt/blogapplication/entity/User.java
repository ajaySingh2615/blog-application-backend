package com.cadt.blogapplication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false, unique = true)  // username must be unique
    private String username;

    @Column(nullable = false, unique = true)  // Email must be unique
    private String email;

    @Column(nullable = false)
    private String password;

    // The Relationship (Many users <-> Many Roles)
    // FetchType.EAGER: When I load a user, load their Roles immediately (we need roles for security check)
    // CascadeType.ALL: If I save a User, save their Roles linkage too.
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",  // The 3rd Join Table name
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;
}
