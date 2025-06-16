package com.example.youtube_viewer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.youtube_viewer.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findByYoutubeId(String youtubeId);
}
