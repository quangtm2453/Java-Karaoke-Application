package com.example.youtube_viewer.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.youtube_viewer.entity.WatchHistory;
import com.example.youtube_viewer.repository.WatchHistoryRepository;
import com.example.youtube_viewer.service.WatchHistoryService;

@Service
public class WatchHistoryServiceImpl implements WatchHistoryService {
    
    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Override
    public WatchHistory save(WatchHistory watchHistory) {
        return watchHistoryRepository.save(watchHistory);
    }

    @Override
    public WatchHistory findById(Long id) {
        return watchHistoryRepository.findById(id).orElse(null);
    }

    @Override
    public List<WatchHistory> findAll() {
        return watchHistoryRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        watchHistoryRepository.deleteById(id);
    }

    @Override
    public List<WatchHistory> findByUserId(Long userId) {
        return watchHistoryRepository.findByUserId(userId);
    }

    @Override
    public List<WatchHistory> findByVideoId(Long videoId) {
        return watchHistoryRepository.findByVideoId(videoId);
    }
}
