package com.vention.fm.domain.model.playlist;

import com.vention.fm.domain.model.track.Track;
import lombok.*;
import com.vention.fm.domain.model.BaseModel;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistTracks extends BaseModel {
    private Playlist playlist;
    private Track track;
    private Integer trackPosition;
}
