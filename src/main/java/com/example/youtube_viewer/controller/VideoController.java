package com.example.youtube_viewer.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.youtube_viewer.service.PlaylistService;
import com.example.youtube_viewer.service.WatchHistoryService;
import com.example.youtube_viewer.service.YouTubeService;

import jakarta.servlet.http.HttpSession;

@Controller
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    private YouTubeService youTubeService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private WatchHistoryService watchHistoryService;

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String pageToken) {
        try {
            Map<String, Object> popularVideos = youTubeService.getPopularVideos("US", pageToken);
            model.addAttribute("videos", popularVideos.get("items"));
            model.addAttribute("nextPageToken", popularVideos.get("nextPageToken"));
            model.addAttribute("prevPageToken", popularVideos.get("prevPageToken"));
        } catch (Exception e) {
            logger.error("Error fetching popular videos", e);
            model.addAttribute("error", "Unable to load videos. Please try again later.");
        }
        return "video/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query,
                        @RequestParam(required = false) String pageToken,
                        Model model) {
        try {
            Map<String, Object> searchResults = youTubeService.searchVideos(query, pageToken);
            model.addAttribute("videos", searchResults.get("items"));
            model.addAttribute("nextPageToken", searchResults.get("nextPageToken"));
            model.addAttribute("prevPageToken", searchResults.get("prevPageToken"));
            model.addAttribute("query", query);
        } catch (Exception e) {
            logger.error("Error searching videos for query: " + query, e);
            model.addAttribute("error", "Unable to search videos. Please try again later.");
            model.addAttribute("query", query);
        }
        return "video/list";
    }

    @GetMapping("/video/{id}")
    public String viewVideo(@PathVariable String id,
                          HttpSession session,
                          Model model) {
        try {
            // Get video details
            Map<String, Object> videoDetails = youTubeService.getVideoDetails(id);
            if (videoDetails == null || videoDetails.isEmpty()) {
                throw new IllegalStateException("Video not found");
            }
            model.addAttribute("video", videoDetails);

            // Get channel info
            String channelId = (String) ((Map)videoDetails.get("snippet")).get("channelId");
            model.addAttribute("channel", youTubeService.getChannelInfo(channelId));

            // Get related videos
            Map<String, Object> relatedVideos = youTubeService.getRelatedVideos(id, null);
            model.addAttribute("relatedVideos", relatedVideos.get("items"));

            // If user is authenticated, get their playlists and add to watch history
            Long userId = (Long) session.getAttribute("userId");
            if (userId != null) {
                model.addAttribute("playlists", playlistService.getPlaylistsByUserId(userId));
                watchHistoryService.addToHistory(userId, id);
            }

            return "video/view";
        } catch (Exception e) {
            logger.error("Error loading video: " + id, e);
            model.addAttribute("error", "Unable to load video. It may be unavailable or have been removed.");
            return "redirect:/";
        }
    }
}
