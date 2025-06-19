package com.example.youtube_viewer.service;

import java.util.List;
import java.util.Optional;

import com.example.youtube_viewer.entity.User;

public interface UserService {
    User registerUser(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    User updateUser(User user);
    void deleteUser(Long userId);
    List<User> getAllUsers();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void updateLastLogin(Long userId);
    boolean verifyPassword(String rawPassword, String encodedPassword);
}
