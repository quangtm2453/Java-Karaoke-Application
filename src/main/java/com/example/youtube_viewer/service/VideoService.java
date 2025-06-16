package com.example.youtube_viewer.service;

import com.example.youtube_viewer.entity.Video;
import java.util.List;
import java.util.Map;

public interface VideoService {
    /**
     * Save video metadata to local database
     */
    Video save(Video video);
    
    /**
     * Find video in local database by id
     */
    Video findById(Long id);
    
    /**
     * Get list of saved videos from local database
     */
    List<Video> findAll();
    
    /**
     * Delete video from local database
     */
    void deleteById(Long id);
    
    /**
     * Update video metadata in local database
     */
    Video update(Video video);
    
    /**
     * Search videos from YouTube API
     * @param query search query
     * @param maxResults maximum number of results to return
     * @return List of videos from YouTube API
     */
    List<Map<String, Object>> searchVideos(String query, int maxResults);
    
    /**
     * Get trending videos from YouTube API
     * @param regionCode country code (e.g., "VN" for Vietnam)
     * @param maxResults maximum number of results to return
     * @return List of trending videos from YouTube API
     */
    List<Map<String, Object>> getTrendingVideos(String regionCode, int maxResults);
    
    /**
     * Get video details from YouTube API by video ID
     * @param videoId YouTube video ID
     * @return Video details from YouTube API
     */
    Map<String, Object> getVideoDetails(String videoId);
}
