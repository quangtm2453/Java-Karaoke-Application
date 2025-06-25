
package com.example.youtube_viewer.dto;

public class PlaylistDto {
    private Long id;
    private String name;    
    private String description;
    private boolean isPublic;

    public PlaylistDto() {
    }
    public PlaylistDto(Long id, String name, String description, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

}
