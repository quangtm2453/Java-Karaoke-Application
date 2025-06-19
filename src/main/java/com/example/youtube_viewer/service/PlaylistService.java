package com.example.youtube_viewer.service;

import com.example.youtube_viewer.entity.Playlist;
import com.example.youtube_viewer.entity.PlaylistVideo;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    Playlist createPlaylist(Playlist playlist);
    Optional<Playlist> getPlaylistById(Long id);
    List<Playlist> getPlaylistsByUserId(Long userId);
    Playlist updatePlaylist(Playlist playlist);
    void deletePlaylist(Long id);
    PlaylistVideo addVideoToPlaylist(Long playlistId, String videoId);
    void removeVideoFromPlaylist(Long playlistId, String videoId);
    List<PlaylistVideo> getPlaylistVideos(Long playlistId);
    boolean isVideoInPlaylist(Long playlistId, String videoId);
}
