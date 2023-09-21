package org.example.domain.model.album;

import lombok.*;
import org.example.domain.model.BaseModel;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumTracks extends BaseModel {
    private UUID albumId;
    private UUID trackId;
    private Integer trackPosition;
}
