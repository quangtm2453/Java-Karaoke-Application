package com.example.youtube_viewer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.youtube_viewer.entity.PlaylistVideo;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, Long> {
    List<PlaylistVideo> findByPlaylistIdOrderByPosition(Long playlistId);
    List<PlaylistVideo> findByPlaylistId(Long playlistId);
    Optional<PlaylistVideo> findByPlaylistIdAndVideoId(Long playlistId, String videoId);
    
    @Query("SELECT MAX(pv.position) FROM PlaylistVideo pv WHERE pv.playlist.id = :playlistId")
    Integer findMaxPositionByPlaylistId(Long playlistId);
    
    boolean existsByPlaylistIdAndVideoId(Long playlistId, String videoId);
    
    void deleteByPlaylistIdAndVideoId(Long playlistId, String videoId);
}