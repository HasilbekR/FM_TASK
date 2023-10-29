package com.vention.fm.domain.dto.album;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlbumUpdateDto {
    private String name;
    private String updatedName;
    private UUID userId;
}
