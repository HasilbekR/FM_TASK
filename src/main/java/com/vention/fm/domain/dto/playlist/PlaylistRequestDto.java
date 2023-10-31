package com.vention.fm.domain.dto.playlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaylistRequestDto {
    private String name;
    private String updatedName;
    private String trackName;
    private Boolean isPublic;
    private Boolean isLiked;
    private UUID userId;
}
