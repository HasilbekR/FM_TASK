package org.example.domain.model.playlist;

import lombok.*;
import org.example.domain.model.BaseModel;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistTracks extends BaseModel{
    private UUID playlistId;
    private UUID trackId;
    private Integer trackPosition;
}
