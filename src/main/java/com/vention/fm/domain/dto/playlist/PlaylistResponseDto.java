package com.vention.fm.domain.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vention.fm.domain.dto.track.TrackDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistResponseDto {
    private String name;
    private Integer likeCount;
    private Integer dislikeCount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TrackDto> tracks;
}
