package com.example.youtube_viewer.convert;

import com.example.youtube_viewer.dto.PlaylistDto;
import com.example.youtube_viewer.entity.Playlist;

public class PlaylistConverter {

    // Convert PlaylistDto to Playlist entity
    public static Playlist convertToEntity(PlaylistDto dto) {
        if (dto == null) {
            return null;
        }
        Playlist playlist = new Playlist();
        playlist.setId(dto.getId());
        playlist.setName(dto.getName());
        playlist.setDescription(dto.getDescription());
        playlist.setPublic(dto.isPublic());
        return playlist;
    }

    // Convert Playlist entity to PlaylistDto
    public static PlaylistDto convertToDto(Playlist entity) {
        if (entity == null) {
            return null;
        }

        PlaylistDto playlistDto = new PlaylistDto();
        playlistDto.setId(entity.getId());
        playlistDto.setName(entity.getName());  
        playlistDto.setDescription(entity.getDescription());
        playlistDto.setPublic(entity.isPublic());
        return playlistDto;
        
    }
}
