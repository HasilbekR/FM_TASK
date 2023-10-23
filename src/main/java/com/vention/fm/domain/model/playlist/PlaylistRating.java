package com.vention.fm.domain.model.playlist;

import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.domain.model.user.User;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistRating extends BaseModel {
    private Playlist playlist;
    private User user;
    private boolean isLiked;
}
