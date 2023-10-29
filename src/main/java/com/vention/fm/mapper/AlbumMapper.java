package com.vention.fm.mapper;

import com.vention.fm.domain.dto.album.AlbumResponseDto;
import com.vention.fm.domain.model.album.Album;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AlbumMapper {
    AlbumResponseDto albumToDto(Album album);
    List<AlbumResponseDto> albumsToDto(List<Album> albums);

}
