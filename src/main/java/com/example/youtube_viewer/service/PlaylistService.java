package com.example.youtube_viewer.service;

import com.example.youtube_viewer.entity.Playlist;
import java.util.List;

public interface PlaylistService {
    Playlist save(Playlist playlist);
    Playlist findById(Long id);
    List<Playlist> findAll();
    void deleteById(Long id);
    Playlist update(Playlist playlist);
    List<Playlist> findByUserId(Long userId);
}
