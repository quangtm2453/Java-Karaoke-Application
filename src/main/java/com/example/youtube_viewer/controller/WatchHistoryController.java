package com.example.youtube_viewer.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.youtube_viewer.entity.WatchHistory;
import com.example.youtube_viewer.service.WatchHistoryService;
import com.example.youtube_viewer.service.YouTubeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    @Autowired
    private YouTubeService youTubeService;

    @GetMapping("/history")
    public String viewHistory(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        List<WatchHistory> history = watchHistoryService.getUserHistory(userId);
        
        // Get video details for the watched videos
        if (!history.isEmpty()) {
            List<String> videoIds = history.stream()
                .map(WatchHistory::getVideoId)
                .collect(Collectors.toList());
                
            List<Map<String, Object>> videoDetails = youTubeService.getVideosDetails(videoIds);
            
            // Match video details with watch history entries
            history.forEach(h -> {
                videoDetails.stream()
                    .filter(v -> v.get("id").equals(h.getVideoId()))
                    .findFirst()
                    .ifPresent(v -> h.setVideoDetails(v));
            });
        }
        
        model.addAttribute("watchHistory", history);
        return "history/list";
    }

    @DeleteMapping("/api/history")
    @ResponseBody
    public ResponseEntity<?> clearHistory(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        try {
            watchHistoryService.clearUserHistory(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/api/history/{videoId}")
    @ResponseBody
    public ResponseEntity<?> removeFromHistory(HttpSession session,
                                             @PathVariable String videoId) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.badRequest().body("User not authenticated");
        }

        try {
            watchHistoryService.removeFromHistory(userId, videoId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
