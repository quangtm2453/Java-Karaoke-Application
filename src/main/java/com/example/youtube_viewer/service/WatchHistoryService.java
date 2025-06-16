package com.example.youtube_viewer.service;

import java.util.List;

import com.example.youtube_viewer.entity.WatchHistory;

public interface WatchHistoryService {
    WatchHistory save(WatchHistory watchHistory);
    WatchHistory findById(Long id);
    List<WatchHistory> findAll();
    void deleteById(Long id);
    List<WatchHistory> findByUserId(Long userId);
    List<WatchHistory> findByVideoId(Long videoId);
}
