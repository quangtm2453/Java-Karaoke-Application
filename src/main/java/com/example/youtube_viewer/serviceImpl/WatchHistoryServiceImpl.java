package com.example.youtube_viewer.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.youtube_viewer.entity.User;
import com.example.youtube_viewer.entity.WatchHistory;
import com.example.youtube_viewer.repository.UserRepository;
import com.example.youtube_viewer.repository.WatchHistoryRepository;
import com.example.youtube_viewer.service.WatchHistoryService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WatchHistoryServiceImpl implements WatchHistoryService {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public WatchHistory addToHistory(Long userId, String videoId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));        List<WatchHistory> existingHistory = watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId);
        existingHistory.stream()
            .filter(h -> h.getVideoId().equals(videoId))
            .findFirst()
            .ifPresent(watchHistoryRepository::delete);

        WatchHistory watchHistory = new WatchHistory();
        watchHistory.setUser(user);
        watchHistory.setVideoId(videoId);
        watchHistory.setWatchedAt(LocalDateTime.now());

        return watchHistoryRepository.save(watchHistory);
    }

    @Override
    public List<WatchHistory> getUserHistory(Long userId) {
        return watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId);
    }    @Override
    @Transactional
    public void clearUserHistory(Long userId) {
        List<WatchHistory> userHistory = watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId);
        watchHistoryRepository.deleteAll(userHistory);
    }

    @Override
    @Transactional
    public void removeFromHistory(Long userId, String videoId) {
        watchHistoryRepository.deleteByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public boolean hasWatched(Long userId, String videoId) {
        return watchHistoryRepository.existsByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public List<WatchHistory> getRecentHistory(Long userId, int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "watchedAt"));
        Page<WatchHistory> page = watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId, pageRequest);
        return page.getContent();
    }
}
