package com.example.youtube_viewer.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.youtube_viewer.convert.PlaylistConverter;
import com.example.youtube_viewer.dto.PlaylistDto;
import com.example.youtube_viewer.entity.Playlist;
import com.example.youtube_viewer.entity.PlaylistVideo;
import com.example.youtube_viewer.repository.PlaylistRepository;
import com.example.youtube_viewer.repository.PlaylistVideoRepository;
import com.example.youtube_viewer.service.PlaylistService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    @Override
    public Playlist createPlaylist(Long userId, String name, String description, boolean isPublic) {
        Playlist playlist = new Playlist();
        playlist.setUserId(userId);
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.setPublic(isPublic);
        playlist.setCreatedAt(LocalDateTime.now());
        playlist.setUpdatedAt(LocalDateTime.now());
        return playlistRepository.save(playlist);
    }

    @Override
    public Optional<Playlist> findById(Long playlistId) {
        return playlistRepository.findById(playlistId);
    }

    @Override
    public List<Playlist> getUserPlaylists(Long userId) {
        return playlistRepository.findByUserIdOrderByUpdatedAtDesc(userId);
    }

    @Override
    public Page<Playlist> getUserPlaylists(Long userId, Pageable pageable) {
        return playlistRepository.findByUserIdOrderByUpdatedAtDesc(userId, pageable);
    }

    @Override
    public List<Playlist> getPublicPlaylists() {
        return playlistRepository.findByIsPublicTrueOrderByUpdatedAtDesc();
    }

    @Override
    public Page<Playlist> getPublicPlaylists(Pageable pageable) {
        return playlistRepository.findByIsPublicTrueOrderByUpdatedAtDesc(pageable);
    }

    @Override
    public Playlist updatePlaylist(Long playlistId, String name, String description, boolean isPublic) {
        return playlistRepository.findById(playlistId)
                .map(playlist -> {
                    playlist.setName(name);
                    playlist.setDescription(description);
                    playlist.setPublic(isPublic);
                    playlist.setUpdatedAt(LocalDateTime.now());
                    return playlistRepository.save(playlist);
                })
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
    }

    @Override
    public void deletePlaylist(Long playlistId, Long userId) {
        playlistRepository.findByIdAndUserId(playlistId, userId)
                .ifPresent(playlist -> {
                    // Delete all videos in playlist first
                    playlistVideoRepository.deleteByPlaylistId(playlistId);
                    // Then delete playlist
                    playlistRepository.delete(playlist);
                });
    }

    @Override
    public boolean isOwner(Long playlistId, Long userId) {
        return playlistRepository.existsByIdAndUserId(playlistId, userId);
    }

    @Override
    public PlaylistVideo addVideoToPlaylist(Long playlistId, String videoId, String videoTitle, 
                                          String videoThumbnail, String channelName) {
        // Check if video already exists in playlist
        if (videoExistsInPlaylist(playlistId, videoId)) {
            throw new RuntimeException("Video already exists in playlist");
        }

        // Get the highest position
        Integer maxPosition = playlistVideoRepository.findMaxPositionByPlaylistId(playlistId);
        int position = (maxPosition != null) ? maxPosition + 1 : 1;

        PlaylistVideo playlistVideo = new PlaylistVideo();
        playlistVideo.setPlaylistId(playlistId);
        playlistVideo.setVideoId(videoId);
        playlistVideo.setVideoTitle(videoTitle);
        playlistVideo.setVideoThumbnail(videoThumbnail);
        playlistVideo.setChannelName(channelName);
        playlistVideo.setPosition(position);
        playlistVideo.setAddedAt(LocalDateTime.now());

        PlaylistVideo saved = playlistVideoRepository.save(playlistVideo);

        // Update playlist updated_at
        playlistRepository.findById(playlistId).ifPresent(playlist -> {
            playlist.setUpdatedAt(LocalDateTime.now());
            playlistRepository.save(playlist);
        });

        return saved;
    }

    @Override
    public void removeVideoFromPlaylist(Long playlistId, String videoId, Long userId) {
        // Check if user owns the playlist
        if (!isOwner(playlistId, userId)) {
            throw new RuntimeException("User does not own this playlist");
        }

        playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId)
                .ifPresent(playlistVideo -> {
                    playlistVideoRepository.delete(playlistVideo);
                    
                    // Update playlist updated_at
                    playlistRepository.findById(playlistId).ifPresent(playlist -> {
                        playlist.setUpdatedAt(LocalDateTime.now());
                        playlistRepository.save(playlist);
                    });
                });
    }

    @Override
    public List<PlaylistVideo> getPlaylistVideos(Long playlistId) {
        return playlistVideoRepository.findByPlaylistIdOrderByPosition(playlistId);
    }

    @Override
    public Page<PlaylistVideo> getPlaylistVideos(Long playlistId, Pageable pageable) {
        return playlistVideoRepository.findByPlaylistIdOrderByPosition(playlistId, pageable);
    }

    @Override
    public boolean videoExistsInPlaylist(Long playlistId, String videoId) {
        return playlistVideoRepository.existsByPlaylistIdAndVideoId(playlistId, videoId);
    }

    @Override
    public void reorderPlaylistVideos(Long playlistId, List<String> videoIds) {
        for (int i = 0; i < videoIds.size(); i++) {
            String videoId = videoIds.get(i);
            int position = i + 1;
            
            playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId)
                    .ifPresent(playlistVideo -> {
                        playlistVideo.setPosition(position);
                        playlistVideoRepository.save(playlistVideo);
                    });
        }

        // Update playlist updated_at
        playlistRepository.findById(playlistId).ifPresent(playlist -> {
            playlist.setUpdatedAt(LocalDateTime.now());
            playlistRepository.save(playlist);
        });
    }

    @Override
    public int getVideoCountInPlaylist(Long playlistId) {
        return playlistVideoRepository.countByPlaylistId(playlistId);
    }

    @Override
    public Playlist createPlaylistDto(PlaylistDto playlistDto) {
        Playlist playlist = new Playlist();
        playlist = PlaylistConverter.convertToEntity(playlistDto);
        playlist.setCreatedAt(LocalDateTime.now()); 
        playlist.setUpdatedAt(LocalDateTime.now());
        return playlistRepository.save(playlist);
    }

    
}