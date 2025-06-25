package com.example.youtube_viewer.service;

import java.util.List;
import java.util.Map;

import java.util.List;

import com.example.youtube_viewer.dto.VideoDto;

public interface YouTubeService {
    List<VideoDto> searchVideos(String query, int maxResults);
    List<VideoDto> getTrendingVideos(int maxResults, String regionCode);
    VideoDto getVideoDetails(String videoId);
}

