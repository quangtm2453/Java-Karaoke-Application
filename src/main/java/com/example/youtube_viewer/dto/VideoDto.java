package com.example.youtube_viewer.dto;

public class VideoDto {
    private String id;
    private String title;
    private String description;
    private String channel;
    private String channelId;
    private String thumbnail;
    private String publishedAt;
    private String duration;
    private String viewCount;
    private String likeCount;
    private String commentCount;

    // Constructors
    public VideoDto() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getViewCount() { return viewCount; }
    public void setViewCount(String viewCount) { this.viewCount = viewCount; }

    public String getLikeCount() { return likeCount; }
    public void setLikeCount(String likeCount) { this.likeCount = likeCount; }

    public String getCommentCount() { return commentCount; }
    public void setCommentCount(String commentCount) { this.commentCount = commentCount; }
}
