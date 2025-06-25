package com.example.youtube_viewer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.youtube_viewer.entity.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdOrderByUpdatedAtDesc(Long userId);
    Page<Playlist> findByUserIdOrderByUpdatedAtDesc(Long userId, Pageable pageable);
    List<Playlist> findByIsPublicTrueOrderByUpdatedAtDesc();
    Page<Playlist> findByIsPublicTrueOrderByUpdatedAtDesc(Pageable pageable);
    Optional<Playlist> findByIdAndUserId(Long id, Long userId);
    boolean existsByIdAndUserId(Long id, Long userId);
}