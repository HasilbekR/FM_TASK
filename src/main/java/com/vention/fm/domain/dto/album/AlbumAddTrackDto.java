package com.vention.fm.domain.dto.album;

import lombok.Data;

import java.util.UUID;

@Data
public class AlbumAddTrackDto {
    private UUID albumId;
    private UUID trackId;
    private UUID ownerId;
}
