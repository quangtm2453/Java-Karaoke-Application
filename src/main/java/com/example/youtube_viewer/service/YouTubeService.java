package com.example.youtube_viewer.service;

import java.util.List;
import java.util.Map;

public interface YouTubeService {
    Map<String, Object> searchVideos(String query, String pageToken);
    Map<String, Object> getVideoDetails(String videoId);
    Map<String, Object> getRelatedVideos(String videoId, String pageToken);
    Map<String, Object> getPopularVideos(String regionCode, String pageToken);
    Map<String, Object> getVideoCategories(String regionCode);
    Map<String, Object> getVideosByCategory(String categoryId, String pageToken);
    Map<String, Object> getChannelInfo(String channelId);
    Map<String, Object> getChannelVideos(String channelId, String pageToken);
    List<Map<String, Object>> getVideosDetails(List<String> videoIds);
}
