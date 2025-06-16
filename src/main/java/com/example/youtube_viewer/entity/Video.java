package com.example.youtube_viewer.entity;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;
    private Long views;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User uploader;
    
    @OneToMany(mappedBy = "video")
    private List<PlaylistVideo> playlists;

    public Video(Long id, String title, String description, String videoUrl, String thumbnailUrl, Date uploadDate,
                 Long views, User uploader, List<PlaylistVideo> playlists) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.uploadDate = uploadDate;
        this.views = views;
        this.uploader = uploader;
        this.playlists = playlists;
    }
    public Video() {
        // Default constructor
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    
    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    
    public Date getUploadDate() { return uploadDate; }
    public void setUploadDate(Date uploadDate) { this.uploadDate = uploadDate; }
    
    public Long getViews() { return views; }
    public void setViews(Long views) { this.views = views; }
    
    public User getUploader() { return uploader; }
    public void setUploader(User uploader) { this.uploader = uploader; }
    
    public List<PlaylistVideo> getPlaylists() { return playlists; }
    public void setPlaylists(List<PlaylistVideo> playlists) { this.playlists = playlists; }
}
