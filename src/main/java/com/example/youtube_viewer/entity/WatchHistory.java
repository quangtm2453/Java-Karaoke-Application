package com.example.youtube_viewer.entity;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "watch_history")
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Video ID is required")
    @Size(max = 20, message = "Video ID must not exceed 20 characters")
    @Column(name = "video_id", length = 20)
    private String videoId;

    @NotBlank(message = "Video title is required")
    @Column(name = "video_title")
    private String videoTitle;

    @Column(name = "video_thumbnail")
    private String videoThumbnail;

    @NotBlank(message = "Channel name is required")
    @Size(max = 100, message = "Channel name must not exceed 100 characters")
    @Column(name = "channel_name", length = 100)
    private String channelName;

    @CreationTimestamp
    @Column(name = "watched_at")
    private LocalDateTime watchedAt;

    @Transient
    private Map<String, Object> videoDetails;

    // Constructors
    public WatchHistory() {}
    public WatchHistory(User user, String videoId, String videoTitle, String videoThumbnail, String channelName) {
        this.user = user;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoThumbnail = videoThumbnail;
        this.channelName = channelName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getVideoTitle() {
        return videoTitle;
    }
    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }
    public String getVideoThumbnail() {
        return videoThumbnail;
    }
    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }
    public String getChannelName() {
        return channelName;
    }
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }
    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }
    public Map<String, Object> getVideoDetails() {
        return videoDetails;
    }
    public void setVideoDetails(Map<String, Object> videoDetails) {
        this.videoDetails = videoDetails;
    }
    
}