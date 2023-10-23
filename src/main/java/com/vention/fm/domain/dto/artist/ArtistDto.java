package com.vention.fm.domain.dto.artist;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtistDto {
    private String name;
    private String url;
    private Integer playcount;
    private Integer listeners;
}
