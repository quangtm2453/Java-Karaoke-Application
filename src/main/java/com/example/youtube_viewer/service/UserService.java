package com.example.youtube_viewer.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import com.example.youtube_viewer.entity.User;

public interface UserService extends UserDetailsService {
    User createUser(String username, String email, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void updateLastLogin(Long userId);
    User save(User user);
}
