package com.vention.fm.domain.model.album;

import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album extends BaseModel {
    private String name;
    private Artist artist;
    private User owner;
}
