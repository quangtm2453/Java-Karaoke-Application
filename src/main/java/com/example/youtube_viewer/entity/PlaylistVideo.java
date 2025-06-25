package com.example.youtube_viewer.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_videos")
public class PlaylistVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "playlist_id", nullable = false)
    private Long playlistId;

    @Column(name = "video_id", nullable = false, length = 20)
    private String videoId;

    @Column(name = "video_title", nullable = false, columnDefinition = "TEXT")
    private String videoTitle;

    @Column(name = "video_thumbnail", columnDefinition = "TEXT")
    private String videoThumbnail;

    @Column(name = "channel_name")
    private String channelName;

    @Column(nullable = false)
    private Integer position = 1;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    // Constructors
    public PlaylistVideo() {}

    public PlaylistVideo(Long playlistId, String videoId, String videoTitle, String videoThumbnail, String channelName, Integer position) {
        this.playlistId = playlistId;
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoThumbnail = videoThumbnail;
        this.channelName = channelName;
        this.position = position;
        this.addedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPlaylistId() { return playlistId; }
    public void setPlaylistId(Long playlistId) { this.playlistId = playlistId; }

    public String getVideoId() { return videoId; }
    public void setVideoId(String videoId) { this.videoId = videoId; }

    public String getVideoTitle() { return videoTitle; }
    public void setVideoTitle(String videoTitle) { this.videoTitle = videoTitle; }

    public String getVideoThumbnail() { return videoThumbnail; }
    public void setVideoThumbnail(String videoThumbnail) { this.videoThumbnail = videoThumbnail; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
