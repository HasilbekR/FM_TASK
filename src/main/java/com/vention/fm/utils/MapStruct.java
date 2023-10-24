package com.vention.fm.utils;

import com.vention.fm.domain.dto.album.AlbumDto;
import com.vention.fm.domain.dto.artist.ArtistDto;
import com.vention.fm.domain.dto.playlist.PlaylistDto;
import com.vention.fm.domain.dto.track.TrackDto;
import com.vention.fm.domain.dto.track.TrackSaveDto;
import com.vention.fm.domain.dto.user.UserDto;
import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.domain.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapStruct {
    MapStruct INSTANCE = Mappers.getMapper(MapStruct.class);
    @Mapping(target = "position", ignore = true)
    TrackDto trackToDto(Track track);
    @Mapping(source = "trackSaveDto.name", target = "name")
    @Mapping(source = "trackSaveDto.url", target = "url")
    @Mapping(target = "artist", ignore = true)
    Track trackDtoToTrack(TrackSaveDto trackSaveDto);
    ArtistDto artistToDto(Artist artist);
    AlbumDto albumToDto(Album album);
    PlaylistDto playlistToDto(Playlist playlist);
    UserDto userToDto(User user);
    List<TrackDto> tracksToDto(List<Track> tracks);
    List<ArtistDto> artistsToDto(List<Artist> artists);
    List<AlbumDto> albumsToDto(List<Album> albums);
    List<PlaylistDto> playlistsToDto(List<Playlist> playlists);
    List<UserDto> usersToDto(List<User> users);
}
