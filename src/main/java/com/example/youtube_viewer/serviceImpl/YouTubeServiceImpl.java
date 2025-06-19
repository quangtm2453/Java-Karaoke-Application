package com.example.youtube_viewer.serviceImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.youtube_viewer.service.YouTubeService;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoCategoryListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

@Service
public class YouTubeServiceImpl implements YouTubeService {

    private static final String APPLICATION_NAME = "YouTube Viewer";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final long MAX_RESULTS = 25L;

    @Value("${youtube.api.key}")
    private String apiKey;

    private YouTube getYouTubeService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JSON_FACTORY, null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Override
    public Map<String, Object> searchVideos(String query, String pageToken) {
        try {
            YouTube.Search.List request = getYouTubeService().search()
                .list("snippet")
                .setKey(apiKey)
                .setQ(query)
                .setType("video")
                .setMaxResults(MAX_RESULTS)
                .setPageToken(pageToken);

            SearchListResponse response = request.execute();
            return createResponseMap(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to search videos: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVideoDetails(String videoId) {
        try {
            YouTube.Videos.List request = getYouTubeService().videos()
                .list("snippet,contentDetails,statistics")
                .setKey(apiKey)
                .setId(videoId);

            VideoListResponse response = request.execute();
            
            if (response.getItems().isEmpty()) {
                throw new RuntimeException("Video not found");
            }

            return videoToMap(response.getItems().get(0));
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get video details: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getRelatedVideos(String videoId, String pageToken) {
        try {
            YouTube.Search.List request = getYouTubeService().search()
                .list("snippet")
                .setKey(apiKey)
                .setRelatedToVideoId(videoId)
                .setType("video")
                .setMaxResults(MAX_RESULTS)
                .setPageToken(pageToken);

            SearchListResponse response = request.execute();
            return createResponseMap(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get related videos: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getPopularVideos(String regionCode, String pageToken) {
        try {
            YouTube.Videos.List request = getYouTubeService().videos()
                .list("snippet,contentDetails,statistics")
                .setKey(apiKey)
                .setChart("mostPopular")
                .setRegionCode(regionCode)
                .setMaxResults(MAX_RESULTS)
                .setPageToken(pageToken);

            VideoListResponse response = request.execute();
            return createResponseMap(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get popular videos: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVideoCategories(String regionCode) {
        try {
            YouTube.VideoCategories.List request = getYouTubeService().videoCategories()
                .list("snippet")
                .setKey(apiKey)
                .setRegionCode(regionCode);

            VideoCategoryListResponse response = request.execute();
            List<Map<String, Object>> categories = response.getItems().stream()
                .map(this::categoryToMap)
                .collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("items", categories);
            return result;
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get video categories: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getVideosByCategory(String categoryId, String pageToken) {
        try {
            YouTube.Videos.List request = getYouTubeService().videos()
                .list("snippet,contentDetails,statistics")
                .setKey(apiKey)
                .setChart("mostPopular")
                .setVideoCategoryId(categoryId)
                .setMaxResults(MAX_RESULTS)
                .setPageToken(pageToken);

            VideoListResponse response = request.execute();
            return createResponseMap(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get videos by category: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getChannelInfo(String channelId) {
        try {
            YouTube.Channels.List request = getYouTubeService().channels()
                .list("snippet,statistics")
                .setKey(apiKey)
                .setId(channelId);

            ChannelListResponse response = request.execute();
            
            if (response.getItems().isEmpty()) {
                throw new RuntimeException("Channel not found");
            }

            return channelToMap(response.getItems().get(0));
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get channel info: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getChannelVideos(String channelId, String pageToken) {
        try {
            YouTube.Search.List request = getYouTubeService().search()
                .list("snippet")
                .setKey(apiKey)
                .setChannelId(channelId)
                .setType("video")
                .setOrder("date")
                .setMaxResults(MAX_RESULTS)
                .setPageToken(pageToken);

            SearchListResponse response = request.execute();
            return createResponseMap(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get channel videos: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Map<String, Object>> getVideosDetails(List<String> videoIds) {
        try {
            YouTube.Videos.List request = getYouTubeService().videos()
                .list("snippet,contentDetails,statistics")
                .setKey(apiKey)
                .setId(String.join(",", videoIds));

            VideoListResponse response = request.execute();
            return response.getItems().stream()
                .map(this::videoToMap)
                .collect(Collectors.toList());
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Failed to get videos details: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> createResponseMap(List<?> items, String nextPageToken, String prevPageToken) {
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("nextPageToken", nextPageToken);
        result.put("prevPageToken", prevPageToken);
        return result;
    }

    private Map<String, Object> videoToMap(Video video) {
        Map<String, Object> videoMap = new HashMap<>();
        videoMap.put("id", video.getId());
        videoMap.put("title", video.getSnippet().getTitle());
        videoMap.put("description", video.getSnippet().getDescription());
        videoMap.put("thumbnail", video.getSnippet().getThumbnails().getHigh().getUrl());
        videoMap.put("channelId", video.getSnippet().getChannelId());
        videoMap.put("channelTitle", video.getSnippet().getChannelTitle());
        videoMap.put("publishedAt", video.getSnippet().getPublishedAt());
        videoMap.put("duration", video.getContentDetails().getDuration());
        videoMap.put("viewCount", video.getStatistics().getViewCount());
        videoMap.put("likeCount", video.getStatistics().getLikeCount());
        return videoMap;
    }

    private Map<String, Object> categoryToMap(VideoCategory category) {
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", category.getId());
        categoryMap.put("title", category.getSnippet().getTitle());
        return categoryMap;
    }

    private Map<String, Object> channelToMap(Channel channel) {
        Map<String, Object> channelMap = new HashMap<>();
        channelMap.put("id", channel.getId());
        channelMap.put("title", channel.getSnippet().getTitle());
        channelMap.put("description", channel.getSnippet().getDescription());
        channelMap.put("thumbnail", channel.getSnippet().getThumbnails().getHigh().getUrl());
        channelMap.put("subscriberCount", channel.getStatistics().getSubscriberCount());
        channelMap.put("videoCount", channel.getStatistics().getVideoCount());
        channelMap.put("viewCount", channel.getStatistics().getViewCount());
        return channelMap;
    }
}
