package com.example.youtube_viewer.service;

import com.example.youtube_viewer.entity.WatchHistory;
import java.util.List;

public interface WatchHistoryService {
    WatchHistory addToHistory(Long userId, String videoId);
    List<WatchHistory> getUserHistory(Long userId);
    void clearUserHistory(Long userId);
    void removeFromHistory(Long userId, String videoId);
    boolean hasWatched(Long userId, String videoId);
    List<WatchHistory> getRecentHistory(Long userId, int limit);
}
