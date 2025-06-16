package com.example.youtube_viewer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "playlist_videos")
public class PlaylistVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;
    
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
    
    private Integer position;

    public PlaylistVideo(Long id, Playlist playlist, Video video, Integer position) {
        this.id = id;
        this.playlist = playlist;
        this.video = video;
        this.position = position;
    }
    public PlaylistVideo() {
        // Default constructor
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }
    
    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }
    
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
}
