package com.example.youtube_viewer.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.youtube_viewer.entity.WatchHistory;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    Page<WatchHistory> findByUserIdOrderByWatchedAtDesc(Long userId, Pageable pageable);
    List<WatchHistory> findByUserIdOrderByWatchedAtDesc(Long userId);
    List<WatchHistory> findByUserIdAndWatchedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
    boolean existsByUserIdAndVideoId(Long userId, String videoId);
    void deleteByUserIdAndVideoId(Long userId, String videoId);
}
