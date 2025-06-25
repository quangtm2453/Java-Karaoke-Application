package com.example.youtube_viewer.serviceImpl;
import com.example.youtube_viewer.dto.VideoDto;
import com.example.youtube_viewer.service.YouTubeService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    @Value("${youtube.api.key}")
    private String apiKey;

    private YouTube youtube;

    @PostConstruct
    public void init() {
        try {
            youtube = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    null)
                    .setApplicationName("YouTube Karaoke App")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize YouTube service", e);
        }
    }

    @Override
    public List<VideoDto> searchVideos(String query, int maxResults) {
        try {
            // Add "karaoke" to search query
            String searchQuery = query + " karaoke";

            // Sử dụng List<String> thay vì String
            YouTube.Search.List search = youtube.search().list(Arrays.asList("snippet"));
            search.setKey(apiKey);
            search.setQ(searchQuery);
            search.setType(Arrays.asList("video")); // Sử dụng List<String>
            search.setMaxResults((long) maxResults);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResults = searchResponse.getItems();

            if (searchResults.isEmpty()) {
                return new ArrayList<>();
            }

            // Get video IDs for additional details
            List<String> videoIds = searchResults.stream()
                    .map(result -> result.getId().getVideoId())
                    .collect(Collectors.toList());

            // Get video details - Sử dụng List<String>
            YouTube.Videos.List videoRequest = youtube.videos().list(Arrays.asList("snippet", "contentDetails", "statistics"));
            videoRequest.setKey(apiKey);
            videoRequest.setId(videoIds); // Truyền List<String> trực tiếp

            VideoListResponse videoResponse = videoRequest.execute();
            List<Video> videos = videoResponse.getItems();

            return videos.stream()
                    .map(this::convertToVideoDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error searching videos", e);
        }
    }

    @Override
    public List<VideoDto> getTrendingVideos(int maxResults, String regionCode) {
        try {
            // Sử dụng List<String> thay vì String
            YouTube.Videos.List request = youtube.videos().list(Arrays.asList("snippet", "contentDetails", "statistics"));
            request.setKey(apiKey);
            request.setChart("mostPopular");
            request.setRegionCode(regionCode);
            request.setVideoCategoryId("10"); // Music category
            request.setMaxResults((long) maxResults);

            VideoListResponse response = request.execute();
            List<Video> videos = response.getItems();

            return videos.stream()
                    .map(this::convertToVideoDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error fetching trending videos", e);
        }
    }

    @Override
    public VideoDto getVideoDetails(String videoId) {
        try {
            // Sử dụng List<String> thay vì String
            YouTube.Videos.List request = youtube.videos().list(Arrays.asList("snippet", "contentDetails", "statistics"));
            request.setKey(apiKey);
            request.setId(Arrays.asList(videoId)); // Chuyển String thành List<String>

            VideoListResponse response = request.execute();
            List<Video> videos = response.getItems();

            if (videos.isEmpty()) {
                return null;
            }

            return convertToVideoDto(videos.get(0));

        } catch (Exception e) {
            throw new RuntimeException("Error fetching video details", e);
        }
    }

    private VideoDto convertToVideoDto(Video video) {
        VideoDto dto = new VideoDto();
        dto.setId(video.getId());
        dto.setTitle(video.getSnippet().getTitle());
        dto.setDescription(video.getSnippet().getDescription());
        dto.setChannel(video.getSnippet().getChannelTitle());
        dto.setChannelId(video.getSnippet().getChannelId());
        dto.setPublishedAt(video.getSnippet().getPublishedAt().toString());
        
        // Thumbnail
        if (video.getSnippet().getThumbnails() != null && 
            video.getSnippet().getThumbnails().getHigh() != null) {
            dto.setThumbnail(video.getSnippet().getThumbnails().getHigh().getUrl());
        }

        // Duration
        if (video.getContentDetails() != null) {
            dto.setDuration(video.getContentDetails().getDuration());
        }

        // Statistics
        if (video.getStatistics() != null) {
            if (video.getStatistics().getViewCount() != null) {
                dto.setViewCount(video.getStatistics().getViewCount().toString());
            }
            if (video.getStatistics().getLikeCount() != null) {
                dto.setLikeCount(video.getStatistics().getLikeCount().toString());
            }
            if (video.getStatistics().getCommentCount() != null) {
                dto.setCommentCount(video.getStatistics().getCommentCount().toString());
            }
        }

        return dto;
    }
}
