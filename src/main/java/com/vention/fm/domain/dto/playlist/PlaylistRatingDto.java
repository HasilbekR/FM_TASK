package com.vention.fm.domain.dto.playlist;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistRatingDto {
    private String playlistName;
    private Boolean isLiked;
    private UUID userId;
}
