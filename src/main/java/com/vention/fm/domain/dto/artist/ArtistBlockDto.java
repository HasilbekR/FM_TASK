package com.vention.fm.domain.dto.artist;

import lombok.Data;

import java.util.UUID;
@Data
public class ArtistBlockDto {
    private UUID artistId;
    private UUID adminId;
}
