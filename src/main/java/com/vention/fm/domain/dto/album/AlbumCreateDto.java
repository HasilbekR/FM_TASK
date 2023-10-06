package com.vention.fm.domain.dto.album;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCreateDto {
    private String name;
    private String artist;
    private UUID ownerId;
}
