package com.vention.fm.domain.dto.artist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistDto {
    private String name;
    private String url;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer playcount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer listeners;
}
