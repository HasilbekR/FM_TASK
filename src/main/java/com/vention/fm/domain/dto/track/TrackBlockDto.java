package com.vention.fm.domain.dto.track;

import lombok.Data;

import java.util.UUID;

@Data
public class TrackBlockDto {
    private UUID trackId;
    private UUID adminId;
}
