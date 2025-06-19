package com.example.youtube_viewer.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.youtube_viewer.entity.Playlist;
import com.example.youtube_viewer.entity.PlaylistVideo;
import com.example.youtube_viewer.entity.User;
import com.example.youtube_viewer.service.PlaylistService;
import com.example.youtube_viewer.service.UserService;
import com.example.youtube_viewer.service.YouTubeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private UserService userService;

    @GetMapping("/playlists")
    public String listPlaylists(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<Playlist> playlists = playlistService.getPlaylistsByUserId(userId);
        model.addAttribute("playlists", playlists);
        return "playlist/list";
    }

    @GetMapping("/playlists/{id}")
    public String viewPlaylist(@PathVariable Long id, HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Playlist> playlist = playlistService.getPlaylistById(id);
        if (playlist.isEmpty()) {
            return "redirect:/playlists";
        }

        // Check if the playlist belongs to the current user
        if (!playlist.get().getUser().getId().equals(userId)) {
            return "redirect:/playlists";
        }

        model.addAttribute("playlist", playlist.get());
        
        // Get video details for each video in the playlist
        List<String> videoIds = playlist.get().getPlaylistVideos().stream()
            .map(PlaylistVideo::getVideoId)
            .collect(Collectors.toList());
            
        if (!videoIds.isEmpty()) {
            List<Map<String, Object>> videoDetails = youTubeService.getVideosDetails(videoIds);
            model.addAttribute("videoDetails", videoDetails);
        }

        return "playlist/view";
    }

    // API Endpoints
    @PostMapping("/api/playlists")
    @ResponseBody
    public ResponseEntity<?> createPlaylist(HttpSession session,
                                          @RequestBody Map<String, String> request) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        try {
            // Get the user
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            // Create new playlist
            Playlist playlist = new Playlist();
            playlist.setName(request.get("name"));
            playlist.setDescription(request.getOrDefault("description", ""));
            playlist.setIsPublic(Boolean.parseBoolean(request.getOrDefault("isPublic", "false")));
            playlist.setUser(userOpt.get());
            
            return ResponseEntity.ok(playlistService.createPlaylist(playlist));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/api/playlists/{playlistId}/videos/{videoId}")
    @ResponseBody
    public ResponseEntity<?> addVideoToPlaylist(HttpSession session,
                                              @PathVariable Long playlistId,
                                              @PathVariable String videoId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        try {
            // Verify playlist belongs to user
            Optional<Playlist> playlist = playlistService.getPlaylistById(playlistId);
            if (playlist.isEmpty() || !playlist.get().getUser().getId().equals(userId)) {
                return ResponseEntity.badRequest().body("Playlist not found or access denied");
            }

            return ResponseEntity.ok(playlistService.addVideoToPlaylist(playlistId, videoId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/playlists/{playlistId}/videos/{videoId}")
    @ResponseBody
    public ResponseEntity<?> removeVideoFromPlaylist(HttpSession session,
                                                   @PathVariable Long playlistId,
                                                   @PathVariable String videoId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        try {
            // Verify playlist belongs to user
            Optional<Playlist> playlist = playlistService.getPlaylistById(playlistId);
            if (playlist.isEmpty() || !playlist.get().getUser().getId().equals(userId)) {
                return ResponseEntity.badRequest().body("Playlist not found or access denied");
            }

            playlistService.removeVideoFromPlaylist(playlistId, videoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
