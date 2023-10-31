package com.vention.fm.mapper;

import com.vention.fm.domain.dto.artist.ArtistDto;
import com.vention.fm.domain.model.artist.Artist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ArtistMapper {
    ArtistDto artistToDto(Artist artist);
    List<ArtistDto> artistsToDto(List<Artist> artists);

}
