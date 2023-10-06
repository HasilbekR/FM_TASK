package com.vention.fm.domain.model.album;

import lombok.*;
import com.vention.fm.domain.model.BaseModel;

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