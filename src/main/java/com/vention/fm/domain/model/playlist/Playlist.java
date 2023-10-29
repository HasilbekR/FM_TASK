package com.vention.fm.domain.model.playlist;

import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.domain.model.user.User;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Playlist extends BaseModel {
    private String name;
    private Boolean isPublic;
    private Integer likeCount;
    private Integer dislikeCount;
    private User owner;
}
