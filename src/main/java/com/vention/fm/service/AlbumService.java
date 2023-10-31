package com.vention.fm.service;

import com.vention.fm.domain.dto.album.AlbumRequestDto;
import com.vention.fm.domain.dto.album.AlbumResponseDto;
import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.user.User;
import com.vention.fm.exception.AccessRestrictedException;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.mapper.AlbumMapper;
import com.vention.fm.repository.album.AlbumRepository;
import com.vention.fm.repository.album.AlbumRepositoryImpl;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

public class AlbumService {
    private final AlbumRepository albumRepository = new AlbumRepositoryImpl();
    private final UserService userService = new UserService();
    private final ArtistService artistService = new ArtistService();
    private final AlbumMapper mapper = Mappers.getMapper(AlbumMapper.class);

    public String save(AlbumRequestDto albumRequestDto) throws AccessRestrictedException {
        User user = userService.getUserState(albumRequestDto.getUserId());
        if (user.getIsBlocked()) {
            throw new AccessRestrictedException("Blocked users are not allowed to create a new album");
        }

        Artist artistState = artistService.getArtistState(albumRequestDto.getArtistName());
        if (artistState.getIsBlocked()) {
            throw new AccessRestrictedException("Blocked artists cannot be added into albums");
        }

        Album album = new Album(albumRequestDto.getName(), artistState, user);
        albumRepository.save(album);
        return "Album with name " + album.getName() + " created successfully";
    }

    public Album getAlbumState(String albumName, UUID ownerId) {
        Album albumState = albumRepository.getAlbumState(albumName, ownerId);
        if (albumState != null) {
            return albumState;
        } else {
            throw new DataNotFoundException("Album not found");
        }
    }

    public Album getAlbum(String albumName, UUID ownerId) {
        Album album = albumRepository.getAlbum(albumName, ownerId);
        if (album != null) {
            return album;
        } else {
            throw new DataNotFoundException("Album not found");
        }
    }

    public List<AlbumResponseDto> getAll(UUID ownerId) {
        List<Album> albums = albumRepository.getAll(ownerId);
        return mapper.albumsToDto(albums); // mapStruct handles null values itself
    }

    public String update(AlbumRequestDto albumDto) {
        Album album = albumRepository.getAlbumState(albumDto.getName(), albumDto.getUserId());
        if (album != null) {
            if (albumDto.getUpdatedName() != null) {
                album.setName(albumDto.getUpdatedName());
            }
            albumRepository.update(album);
            return "Album successfully updated";
        } else {
            throw new DataNotFoundException("Album with name " + albumDto.getName() + " not found");
        }
    }

    public String delete(String albumName, String ownerId) {
        Album album = albumRepository.getAlbumState(albumName, UUID.fromString(ownerId));
        if (album != null) {
            albumRepository.delete(album.getId());
            return "Album with name " + albumName + " deleted successfully";
        } else {
            return "Album with name " + albumName + " not found";
        }
    }
}
