package com.example.youtube_viewer.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.youtube_viewer.entity.Playlist;
import com.example.youtube_viewer.entity.PlaylistVideo;
import com.example.youtube_viewer.repository.PlaylistRepository;
import com.example.youtube_viewer.repository.PlaylistVideoRepository;
import com.example.youtube_viewer.service.PlaylistService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    @Override
    public Playlist createPlaylist(Playlist playlist) {
        playlist.setCreatedAt(LocalDateTime.now());
        return playlistRepository.save(playlist);
    }

    @Override
    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }

    @Override
    public List<Playlist> getPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUserId(userId);
    }

    @Override
    public Playlist updatePlaylist(Playlist playlist) {
        if (!playlistRepository.existsById(playlist.getId())) {
            throw new RuntimeException("Playlist not found");
        }
        return playlistRepository.save(playlist);
    }

    @Override
    @Transactional
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    @Override
    @Transactional
    public PlaylistVideo addVideoToPlaylist(Long playlistId, String videoId) {
        Playlist playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new RuntimeException("Playlist not found"));

        if (isVideoInPlaylist(playlistId, videoId)) {
            throw new RuntimeException("Video already exists in playlist");
        }

        PlaylistVideo playlistVideo = new PlaylistVideo();
        playlistVideo.setPlaylist(playlist);
        playlistVideo.setVideoId(videoId);
        playlistVideo.setAddedAt(LocalDateTime.now());

        return playlistVideoRepository.save(playlistVideo);
    }

    @Override
    @Transactional
    public void removeVideoFromPlaylist(Long playlistId, String videoId) {
        PlaylistVideo playlistVideo = playlistVideoRepository
            .findByPlaylistIdAndVideoId(playlistId, videoId)
            .orElseThrow(() -> new RuntimeException("Video not found in playlist"));
        
        playlistVideoRepository.delete(playlistVideo);
    }

    @Override
    public List<PlaylistVideo> getPlaylistVideos(Long playlistId) {
        return playlistVideoRepository.findByPlaylistId(playlistId);
    }

    @Override
    public boolean isVideoInPlaylist(Long playlistId, String videoId) {
        return playlistVideoRepository.existsByPlaylistIdAndVideoId(playlistId, videoId);
    }
}
