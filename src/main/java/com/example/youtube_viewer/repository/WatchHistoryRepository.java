package com.example.youtube_viewer.repository;

import com.example.youtube_viewer.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    Page<WatchHistory> findByUserIdOrderByWatchedAtDesc(Long userId, Pageable pageable);
    Optional<WatchHistory> findByUserIdAndVideoId(Long userId, String videoId);
    Optional<WatchHistory> findByIdAndUserId(Long id, Long userId);
    boolean existsByUserIdAndVideoId(Long userId, String videoId);
    void deleteByUserId(Long userId);
}
