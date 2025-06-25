package com.example.youtube_viewer.convert;

import java.util.List;

import com.google.api.services.youtube.model.Entity;

public class DtoConverter {
    
    public Entity convertToEntity(Object dto) {
        if (dto instanceof Entity) {
            return (Entity) dto;
        }
        // Implement conversion logic for other DTO types as needed
        throw new IllegalArgumentException("Unsupported DTO type: " + dto.getClass().getName());
    }

    public List<Entity> convertToEntityList(List<?> dtoList) {
        return dtoList.stream()
                      .map(this::convertToEntity)
                      .toList();
    }
}
