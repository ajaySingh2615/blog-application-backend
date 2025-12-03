package com.cadt.blogapplication.controller;

import com.cadt.blogapplication.entity.Role;
import com.cadt.blogapplication.entity.User;
import com.cadt.blogapplication.payload.LoginDto;
import com.cadt.blogapplication.payload.RegisterDto;
import com.cadt.blogapplication.repository.RoleRepository;
import com.cadt.blogapplication.repository.UserRepository;
import com.cadt.blogapplication.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // We inject the Manager here! (This works because of the @Bean you just asked about)
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // REGISTER API
    // POST /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

        // 1. check if username exists
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // 2. Check if email exists
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // 3. Create User Object
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());

        // 4. Encrypt password (Crucial step)
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        // 5. Assign Role (Default to ROLE_USER)
        // Note: You must ensure "ROLE_USER" exists in your roles table first!
        // for this test, we will handle the empty DB case safely:
        Role roles = roleRepository.findByName("ROLE_USER").get();
        // WARNING: If DB is empty, this .get() will crash.
        // We will fix that manually in DB first.

        user.setRoles(Collections.singleton(roles));

        // 6. Save
        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully!", HttpStatus.OK);
    }

    // Login API
    @PostMapping("/login")  // URL: /api/auth/login
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {

        // 1. The manager attempts to log in
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()
                ));

        // If successfully, store the authenticated user in the Security Context (Memory)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate token
        String token = jwtTokenProvider.generateToken(authentication);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }


}
