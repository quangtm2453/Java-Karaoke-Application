package com.example.youtube_viewer.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "playlists")
public class Playlist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    private String description; 
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "playlist")
    private List<PlaylistVideo> videos;

    public Playlist(Long id, String name, String description, User user, List<PlaylistVideo> videos) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.videos = videos;
    }
    public Playlist() {
        // Default constructor
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public List<PlaylistVideo> getVideos() { return videos; }
    public void setVideos(List<PlaylistVideo> videos) { this.videos = videos; }
}
