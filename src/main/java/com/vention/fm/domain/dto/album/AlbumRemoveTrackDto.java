package com.vention.fm.domain.dto.album;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumRemoveTrackDto {
    private String albumName;
    private String trackName;
    private UUID userId;
}
