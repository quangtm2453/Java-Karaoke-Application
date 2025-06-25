package com.example.youtube_viewer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import com.example.youtube_viewer.entity.PlaylistVideo;

@Repository
public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideo, Long> {
    List<PlaylistVideo> findByPlaylistIdOrderByPosition(Long playlistId);
    Page<PlaylistVideo> findByPlaylistIdOrderByPosition(Long playlistId, Pageable pageable);
    Optional<PlaylistVideo> findByPlaylistIdAndVideoId(Long playlistId, String videoId);
    boolean existsByPlaylistIdAndVideoId(Long playlistId, String videoId);
    void deleteByPlaylistId(Long playlistId);
    int countByPlaylistId(Long playlistId);
    
    @Query("SELECT MAX(pv.position) FROM PlaylistVideo pv WHERE pv.playlistId = :playlistId")
    Integer findMaxPositionByPlaylistId(@Param("playlistId") Long playlistId);
} 
