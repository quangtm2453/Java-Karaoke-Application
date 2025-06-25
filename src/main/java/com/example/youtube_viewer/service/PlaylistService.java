package com.example.youtube_viewer.service;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.youtube_viewer.dto.PlaylistDto;
import com.example.youtube_viewer.entity.Playlist;
import com.example.youtube_viewer.entity.PlaylistVideo;

public interface PlaylistService {
   Playlist createPlaylist(Long userId, String name, String description, boolean isPublic);
    Optional<Playlist> findById(Long playlistId);
    List<Playlist> getUserPlaylists(Long userId);
    Page<Playlist> getUserPlaylists(Long userId, Pageable pageable);
    List<Playlist> getPublicPlaylists();
    Page<Playlist> getPublicPlaylists(Pageable pageable);
    Playlist updatePlaylist(Long playlistId, String name, String description, boolean isPublic);
    void deletePlaylist(Long playlistId, Long userId);
    boolean isOwner(Long playlistId, Long userId);
    
    // Playlist Video operations
    PlaylistVideo addVideoToPlaylist(Long playlistId, String videoId, String videoTitle, 
                                   String videoThumbnail, String channelName);
    void removeVideoFromPlaylist(Long playlistId, String videoId, Long userId);
    List<PlaylistVideo> getPlaylistVideos(Long playlistId);
    Page<PlaylistVideo> getPlaylistVideos(Long playlistId, Pageable pageable);
    boolean videoExistsInPlaylist(Long playlistId, String videoId);
    void reorderPlaylistVideos(Long playlistId, List<String> videoIds);
    int getVideoCountInPlaylist(Long playlistId);

    Playlist createPlaylistDto(PlaylistDto playlistDto);
}
