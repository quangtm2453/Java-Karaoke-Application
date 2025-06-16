package com.example.youtube_viewer.entity;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    private String email;
    private String fullName;
    private String avatarUrl;
    
    @OneToMany(mappedBy = "user")
    private List<Playlist> playlists;
    
    @OneToMany(mappedBy = "user")
    private List<WatchHistory> watchHistory;

    public User(Long id, String username, String password, String email, String fullName, String avatarUrl,
            List<Playlist> playlists, List<WatchHistory> watchHistory) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.avatarUrl = avatarUrl;
        this.playlists = playlists;
        this.watchHistory = watchHistory;
    }
    public User() {
        // Default constructor
    }
    // Getters and Setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    public void setUsername(String username) { 
        this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    
    public List<Playlist> getPlaylists() { return playlists; }
    public void setPlaylists(List<Playlist> playlists) { this.playlists = playlists; }
    
    public List<WatchHistory> getWatchHistory() { return watchHistory; }
    public void setWatchHistory(List<WatchHistory> watchHistory) { this.watchHistory = watchHistory; }
}
