package com.vention.fm.domain.model.album;

import com.vention.fm.domain.model.BaseModel;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Album extends BaseModel {
    private String name;
    private UUID artistId;
    private UUID ownerId;
}
