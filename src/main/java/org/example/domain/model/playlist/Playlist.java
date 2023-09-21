package org.example.domain.model.playlist;

import lombok.*;
import org.example.domain.model.BaseModel;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Playlist extends BaseModel {
    private String name;
    private Boolean isPrivate;
    private Boolean isPublic;
    private Integer likeCount;
    private Integer dislikeCount;
}
