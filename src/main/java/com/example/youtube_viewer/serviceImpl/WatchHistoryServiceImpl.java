package com.example.youtube_viewer.serviceImpl;
import com.example.youtube_viewer.entity.WatchHistory;
import com.example.youtube_viewer.repository.WatchHistoryRepository;
import com.example.youtube_viewer.service.WatchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WatchHistoryServiceImpl implements WatchHistoryService {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Override
    public WatchHistory addToHistory(Long userId, String videoId, String videoTitle, 
                                   String videoThumbnail, String channelName) {
        // Check if video already exists in history
        Optional<WatchHistory> existing = watchHistoryRepository
                .findByUserIdAndVideoId(userId, videoId);

        if (existing.isPresent()) {
            // Update watched_at timestamp
            WatchHistory history = existing.get();
            history.setWatchedAt(LocalDateTime.now());
            return watchHistoryRepository.save(history);
        } else {
            // Create new history entry
            WatchHistory history = new WatchHistory();
            history.setUserId(userId);
            history.setVideoId(videoId);
            history.setVideoTitle(videoTitle);
            history.setVideoThumbnail(videoThumbnail);
            history.setChannelName(channelName);
            history.setWatchedAt(LocalDateTime.now());
            return watchHistoryRepository.save(history);
        }
    }

    @Override
    public Page<WatchHistory> getUserHistory(Long userId, Pageable pageable) {
        return watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId, pageable);
    }

    @Override
    public List<WatchHistory> getUserHistory(Long userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("watchedAt").descending());
        return watchHistoryRepository.findByUserIdOrderByWatchedAtDesc(userId, pageable).getContent();
    }

    @Override
    public void removeFromHistory(Long historyId, Long userId) {
        watchHistoryRepository.findByIdAndUserId(historyId, userId)
                .ifPresent(watchHistoryRepository::delete);
    }

    @Override
    public void clearHistory(Long userId) {
        watchHistoryRepository.deleteByUserId(userId);
    }

    @Override
    public boolean existsByUserIdAndVideoId(Long userId, String videoId) {
        return watchHistoryRepository.existsByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public void updateWatchedAt(Long userId, String videoId) {
        watchHistoryRepository.findByUserIdAndVideoId(userId, videoId)
                .ifPresent(history -> {
                    history.setWatchedAt(LocalDateTime.now());
                    watchHistoryRepository.save(history);
                });
    }
}
