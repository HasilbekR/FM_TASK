package com.vention.fm.mapper;

import com.vention.fm.domain.dto.playlist.PlaylistResponseDto;
import com.vention.fm.domain.model.playlist.Playlist;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper
public interface PlaylistMapper {
    PlaylistResponseDto playlistToDto(Playlist playlist);
    List<PlaylistResponseDto> playlistsToDto(List<Playlist> playlists);
}
