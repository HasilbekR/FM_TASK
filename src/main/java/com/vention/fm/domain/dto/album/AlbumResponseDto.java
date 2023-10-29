package com.vention.fm.domain.dto.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vention.fm.domain.dto.artist.ArtistDto;
import com.vention.fm.domain.dto.track.TrackDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumResponseDto {
    private String name;
    private ArtistDto artist;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TrackDto> tracks;
}
