package com.vention.fm.domain.dto.playlist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistUpdateDto {
    private UUID id;
    private String name;
    private Boolean isPublic;
    private UUID ownerId;
}
