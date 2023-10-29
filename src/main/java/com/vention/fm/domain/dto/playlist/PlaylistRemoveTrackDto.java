package com.vention.fm.domain.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistRemoveTrackDto {
    private String playlistName;
    private String trackName;
    private UUID userId;
}
