package com.vention.fm.mapper;

import com.vention.fm.domain.dto.track.TrackDto;
import com.vention.fm.domain.model.track.Track;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TrackMapper {
    @Mapping(target = "position", ignore = true)
    TrackDto trackToDto(Track track);

    @Mapping(source = "trackDto.name", target = "name")
    @Mapping(source = "trackDto.url", target = "url")
    @Mapping(target = "artist", ignore = true)
    Track trackDtoToTrack(TrackDto trackDto);

    List<TrackDto> tracksToDto(List<Track> tracks);
}
