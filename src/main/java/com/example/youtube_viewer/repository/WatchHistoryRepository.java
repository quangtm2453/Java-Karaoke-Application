package com.example.youtube_viewer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.youtube_viewer.entity.WatchHistory;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    List<WatchHistory> findByUserId(Long userId);
    List<WatchHistory> findByVideoId(Long videoId);
}
