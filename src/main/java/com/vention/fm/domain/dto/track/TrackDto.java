package com.vention.fm.domain.dto.track;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vention.fm.domain.dto.artist.ArtistDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackDto {

    private String name;
    private String url;
    private Integer duration;
    private Integer playcount;
    private Integer listeners;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer position;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ArtistDto artist;
}
