package com.vention.fm.domain.model.track;

import lombok.*;
import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.domain.model.artist.Artist;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Track extends BaseModel {
    private String name;
    private String url;
    private Integer duration;
    private Integer playcount;
    private Integer listeners;
    private Artist artist;
}
