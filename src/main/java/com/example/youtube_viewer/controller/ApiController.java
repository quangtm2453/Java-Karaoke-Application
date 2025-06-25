package com.example.youtube_viewer.controller;
import com.example.youtube_viewer.dto.VideoDto;
import com.example.youtube_viewer.entity.Playlist;  
import com.example.youtube_viewer.entity.PlaylistVideo;
import com.example.youtube_viewer.entity.User;
import com.example.youtube_viewer.entity.WatchHistory;
import com.example.youtube_viewer.service.PlaylistService;
import com.example.youtube_viewer.service.UserService;
import com.example.youtube_viewer.service.WatchHistoryService;
import com.example.youtube_viewer.service.YouTubeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private UserService userService;

    @Autowired
    private WatchHistoryService watchHistoryService;

    @Autowired
    private PlaylistService playlistService;

    // YouTube API endpoints
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchVideos(
            @RequestParam String q,
            @RequestParam(defaultValue = "12") int maxResults) {
        try {
            List<VideoDto> videos = youTubeService.searchVideos(q, maxResults);
            Map<String, Object> response = new HashMap<>();
            response.put("videos", videos);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("videos", List.of());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingVideos(
            @RequestParam(defaultValue = "12") int maxResults,
            @RequestParam(defaultValue = "US") String regionCode) {
        try {
            List<VideoDto> videos = youTubeService.getTrendingVideos(maxResults, regionCode);
            Map<String, Object> response = new HashMap<>();
            response.put("videos", videos);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("videos", List.of());
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<VideoDto> getVideoDetails(@PathVariable String videoId) {
        try {
            VideoDto video = youTubeService.getVideoDetails(videoId);
            if (video != null) {
                return ResponseEntity.ok(video);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Watch History endpoints
    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getHistory(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        User user = getCurrentUser(auth);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<WatchHistory> historyPage = watchHistoryService.getUserHistory(user.getId(), pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("history", historyPage.getContent());
        response.put("totalPages", historyPage.getTotalPages());
        response.put("totalElements", historyPage.getTotalElements());
        response.put("currentPage", page);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/history/clear")
    public ResponseEntity<Map<String, Object>> clearHistory(Authentication auth) {
        User user = getCurrentUser(auth);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            watchHistoryService.clearHistory(user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/history/remove/{historyId}")
    public ResponseEntity<Map<String, Object>> removeFromHistory(
            @PathVariable Long historyId, Authentication auth) {
        
        User user = getCurrentUser(auth);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            watchHistoryService.removeFromHistory(historyId, user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    // Playlist endpoints
    @GetMapping("/playlists")
    public ResponseEntity<Map<String, Object>> getUserPlaylists(Authentication auth) {
        User user = getCurrentUser(auth);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            List<Playlist> playlists = playlistService.getUserPlaylists(user.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("playlists", playlists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("playlists", List.of());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/playlist")
    public ResponseEntity<Map<String, Object>> createPlaylist(
            @RequestBody Map<String, Object> request, Authentication auth) {
        
        User user = getCurrentUser(auth);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            String name = (String) request.get("name");
            String description = (String) request.getOrDefault("description", "");
            boolean isPublic = (Boolean) request.getOrDefault("isPublic", false);

            Playlist playlist = playlistService.createPlaylist(user.getId(), name, description, isPublic);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("playlist", playlist);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/playlist/{playlistId}/add")
    public ResponseEntity<Map<String, Object>> addVideoToPlaylist(
            @PathVariable Long playlistId,
            @RequestBody Map<String, Object> request,
            Authentication auth) {
        
        User user = getCurrentUser(auth);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        if (!playlistService.isOwner(playlistId, user.getId())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "You don't have permission to add videos to this playlist");
            return ResponseEntity.status(403).body(response);
        }

        try {
            String videoId = (String) request.get("videoId");
            String videoTitle = (String) request.get("videoTitle");
            String videoThumbnail = (String) request.get("videoThumbnail");
            String channelName = (String) request.get("channelName");

            PlaylistVideo playlistVideo = playlistService.addVideoToPlaylist(
                    playlistId, videoId, videoTitle, videoThumbnail, channelName);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("playlistVideo", playlistVideo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    private User getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }
        return userService.findByUsername(auth.getName()).orElse(null);
    }

    @PostMapping("/playlist/{playlistId}/remove")
    public ResponseEntity<Map<String, Object>> deletePlaylist(
        @PathVariable Long playlistId,
        Authentication auth) {
    
    User user = getCurrentUser(auth);
    Map<String, Object> response = new HashMap<>();
    
    try {
        if (!playlistService.isOwner(playlistId, user.getId())) {
            response.put("success", false);
            response.put("error", "Bạn không có quyền xóa playlist này");
            return ResponseEntity.status(403).body(response);
        }
        
        playlistService.deletePlaylist(playlistId, user.getId());
        response.put("success", true);
        return ResponseEntity.ok(response);
        
    } catch (Exception e) {
        response.put("success", false);
        response.put("error", e.getMessage());
        return ResponseEntity.ok(response);
    }
    }
}
