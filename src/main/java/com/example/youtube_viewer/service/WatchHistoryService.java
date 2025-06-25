package com.example.youtube_viewer.service;

import com.example.youtube_viewer.entity.WatchHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WatchHistoryService {
    WatchHistory addToHistory(Long userId, String videoId, String videoTitle, 
                             String videoThumbnail, String channelName);
    Page<WatchHistory> getUserHistory(Long userId, Pageable pageable);
    List<WatchHistory> getUserHistory(Long userId, int limit);
    void removeFromHistory(Long historyId, Long userId);
    void clearHistory(Long userId);
    boolean existsByUserIdAndVideoId(Long userId, String videoId);
    void updateWatchedAt(Long userId, String videoId);
}

