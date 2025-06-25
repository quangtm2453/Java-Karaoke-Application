package com.example.youtube_viewer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "watch_history")
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "video_id", nullable = false, length = 20)
    private String videoId;

    @Column(name = "video_title", nullable = false, columnDefinition = "TEXT")
    private String videoTitle;

    @Column(name = "video_thumbnail", columnDefinition = "TEXT")
    private String videoThumbnail;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "watched_at")
    private LocalDateTime watchedAt;

    // Constructors
    public WatchHistory() {}

    public WatchHistory(Long userId, String videoId, String videoTitle, String videoThumbnail, String channelName) {
        this.userId = userId;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoThumbnail = videoThumbnail;
        this.channelName = channelName;
        this.watchedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getVideoTitle() { return videoTitle; }
    public void setVideoTitle(String videoTitle) { this.videoTitle = videoTitle; }

    public String getVideoThumbnail() { return videoThumbnail; }
    public void setVideoThumbnail(String videoThumbnail) { this.videoThumbnail = videoThumbnail; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public LocalDateTime getWatchedAt() { return watchedAt; }
    public void setWatchedAt(LocalDateTime watchedAt) { this.watchedAt = watchedAt; }
}
