package com.example.youtube_viewer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.youtube_viewer.service.YouTubeService;

@RestController
@RequestMapping("/api/youtube")
public class YouTubeApiController {

    @Autowired
    private YouTubeService youTubeService;

    @GetMapping("/search")
    public ResponseEntity<?> searchVideos(@RequestParam String query,
                                        @RequestParam(required = false) String pageToken) {
        try {
            Map<String, Object> results = youTubeService.searchVideos(query, pageToken);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/videos/{videoId}")
    public ResponseEntity<?> getVideoDetails(@PathVariable String videoId) {
        try {
            Map<String, Object> video = youTubeService.getVideoDetails(videoId);
            return ResponseEntity.ok(video);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/videos/{videoId}/related")
    public ResponseEntity<?> getRelatedVideos(@PathVariable String videoId,
                                            @RequestParam(required = false) String pageToken) {
        try {
            Map<String, Object> videos = youTubeService.getRelatedVideos(videoId, pageToken);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/videos/popular")
    public ResponseEntity<?> getPopularVideos(@RequestParam(defaultValue = "US") String regionCode,
                                            @RequestParam(required = false) String pageToken) {
        try {
            Map<String, Object> videos = youTubeService.getPopularVideos(regionCode, pageToken);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getVideoCategories(@RequestParam(defaultValue = "US") String regionCode) {
        try {
            Map<String, Object> categories = youTubeService.getVideoCategories(regionCode);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/categories/{categoryId}/videos")
    public ResponseEntity<?> getVideosByCategory(@PathVariable String categoryId,
                                               @RequestParam(required = false) String pageToken) {
        try {
            Map<String, Object> videos = youTubeService.getVideosByCategory(categoryId, pageToken);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/channels/{channelId}")
    public ResponseEntity<?> getChannelInfo(@PathVariable String channelId) {
        try {
            Map<String, Object> channel = youTubeService.getChannelInfo(channelId);
            return ResponseEntity.ok(channel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/channels/{channelId}/videos")
    public ResponseEntity<?> getChannelVideos(@PathVariable String channelId,
                                            @RequestParam(required = false) String pageToken) {
        try {
            Map<String, Object> videos = youTubeService.getChannelVideos(channelId, pageToken);
            return ResponseEntity.ok(videos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
