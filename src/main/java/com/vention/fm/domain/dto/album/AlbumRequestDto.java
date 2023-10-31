package com.vention.fm.domain.dto.album;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumRequestDto {
    private String name;
    private String trackName;
    private String artistName;
    private String updatedName;
    private UUID userId;
}
