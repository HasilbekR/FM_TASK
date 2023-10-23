package com.vention.fm.domain.dto.track;

import lombok.Data;

@Data
public class TrackSaveDto {
    private String name;
    private int duration;
    private String artist;
    private String url;
}
