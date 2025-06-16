package com.example.youtube_viewer.service;

import com.example.youtube_viewer.entity.PlaylistVideo;
import java.util.List;

public interface PlaylistVideoService {
    PlaylistVideo save(PlaylistVideo playlistVideo);
    PlaylistVideo findById(Long id);
    List<PlaylistVideo> findAll();
    void deleteById(Long id);
    List<PlaylistVideo> findByPlaylistId(Long playlistId);
    List<PlaylistVideo> findByVideoId(Long videoId);
}
