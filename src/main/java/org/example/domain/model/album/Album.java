package org.example.domain.model.album;

import lombok.*;
import org.example.domain.model.BaseModel;

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
