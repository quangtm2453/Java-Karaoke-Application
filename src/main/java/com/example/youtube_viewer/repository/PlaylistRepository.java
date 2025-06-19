package com.example.youtube_viewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.youtube_viewer.entity.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Playlist> findByIsPublicTrueOrderByCreatedAtDesc();
    List<Playlist> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);
    List<Playlist> findByUserId(Long userId);
}
