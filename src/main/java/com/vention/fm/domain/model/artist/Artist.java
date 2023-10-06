package com.vention.fm.domain.model.artist;

import com.vention.fm.domain.model.BaseModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist extends BaseModel {
    private String name;
    private String url;
    private Integer playcount;
    private Integer listeners;
}
