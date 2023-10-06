package com.vention.fm.domain.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistAddTrackDto {
    private UUID playlistId;
    private UUID trackId;
    private UUID ownerId;
}
