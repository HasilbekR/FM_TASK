package com.vention.fm.domain.model.album;

import com.vention.fm.domain.model.track.Track;
import lombok.*;
import com.vention.fm.domain.model.BaseModel;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumTracks extends BaseModel {
    private Album album;
    private Track track;
    private Integer trackPosition;
}
