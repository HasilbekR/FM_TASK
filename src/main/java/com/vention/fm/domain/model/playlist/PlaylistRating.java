package com.vention.fm.domain.model.playlist;

import com.vention.fm.domain.model.BaseModel;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistRating extends BaseModel {
    private UUID playlistId;
    private UUID userId;
    private Boolean isLiked;
}
