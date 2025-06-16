package com.example.youtube_viewer.entity;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "watch_history")
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date watchDate;
    
    private Long watchDuration;

    public WatchHistory(Long id, User user, Video video, Date watchDate, Long watchDuration) {
        this.id = id;
        this.user = user;
        this.video = video;
        this.watchDate = watchDate;
        this.watchDuration = watchDuration;
    }

    public WatchHistory() {
        // Default constructor
    }
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Video getVideo() { return video; }
    public void setVideo(Video video) { this.video = video; }
    
    public Date getWatchDate() { return watchDate; }
    public void setWatchDate(Date watchDate) { this.watchDate = watchDate; }
    
    public Long getWatchDuration() { return watchDuration; }
    public void setWatchDuration(Long watchDuration) { this.watchDuration = watchDuration; }
}
