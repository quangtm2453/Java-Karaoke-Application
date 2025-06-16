package com.example.youtube_viewer.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.youtube_viewer.entity.Video;
import com.example.youtube_viewer.repository.VideoRepository;
import com.example.youtube_viewer.service.VideoService;

@Service
public class VideoServiceImpl implements VideoService {
    
    @Autowired
    private VideoRepository videoRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private final String YOUTUBE_API_KEY = "YOUR_API_KEY"; // Will be moved to configuration
    private final String YOUTUBE_API_BASE_URL = "https://www.googleapis.com/youtube/v3";

    @Override
    public Video save(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public Video findById(Long id) {
        return videoRepository.findById(id).orElse(null);
    }

    @Override
    public List<Video> findAll() {
        return videoRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        videoRepository.deleteById(id);
    }

    @Override
    public Video update(Video video) {
        Video existingVideo = findById(video.getId());
        if (existingVideo != null) {
            return videoRepository.save(video);
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> searchVideos(String query, int maxResults) {
        String url = String.format("%s/search?part=snippet&q=%s&maxResults=%d&type=video&key=%s",
            YOUTUBE_API_BASE_URL, query, maxResults, YOUTUBE_API_KEY);
        // TODO: Implement YouTube API call and response mapping
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getTrendingVideos(String regionCode, int maxResults) {
        String url = String.format("%s/videos?part=snippet,statistics&chart=mostPopular&regionCode=%s&maxResults=%d&key=%s",
            YOUTUBE_API_BASE_URL, regionCode, maxResults, YOUTUBE_API_KEY);
        // TODO: Implement YouTube API call and response mapping
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getVideoDetails(String videoId) {
        String url = String.format("%s/videos?part=snippet,statistics,contentDetails&id=%s&key=%s",
            YOUTUBE_API_BASE_URL, videoId, YOUTUBE_API_KEY);
        // TODO: Implement YouTube API call and response mapping
        return null;
    }
}
