package com.vention.fm.domain.dto.album;

import lombok.Data;

import java.util.UUID;

@Data
public class AlbumAddTrackDto {
    private String albumName;
    private String trackName;
    private UUID userId;
}
