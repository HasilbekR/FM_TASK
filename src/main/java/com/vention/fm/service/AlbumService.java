package com.vention.fm.service;

import com.vention.fm.domain.dto.album.AlbumCreateDto;
import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.repository.album.AlbumRepository;
import com.vention.fm.repository.album.AlbumRepositoryImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public class AlbumService {
    private final AlbumRepository albumRepository = new AlbumRepositoryImpl();
    private final UserService userService = new UserService();
    private final ArtistService artistService = new ArtistService();

    public Album getAlbumByName(String name, UUID ownerId) {
        return albumRepository.getByName(name, ownerId);
    }

    public List<Album> getAll(UUID ownerId) {
        return albumRepository.getAll(ownerId);
    }

    public void save(AlbumCreateDto albumCreateDto) throws AccessDeniedException {
        UUID ownerId = albumCreateDto.getOwnerId();
        Boolean isBlocked = userService.isBlocked(ownerId);
        if (isBlocked) throw new AccessDeniedException("Blocked users are not allowed to create a new album");

        Artist artistByName = artistService.getArtistByName(albumCreateDto.getArtist());
        Boolean isArtistBlocked = artistService.isBlocked(artistByName.getId());
        if (isArtistBlocked) throw new AccessDeniedException("Blocked artists cannot be added into albums");

        Album album = new Album(albumCreateDto.getName(), artistByName.getId(), albumCreateDto.getOwnerId());
        albumRepository.save(album);
    }

    public UUID getArtistId(UUID albumId) {
        return albumRepository.getArtistId(albumId);
    }

    public UUID getOwnerId(UUID albumId) {
        return albumRepository.getOwnerId(albumId);
    }

    public void delete(String albumId, String ownerId) throws AccessDeniedException {
        UUID ownerId1 = albumRepository.getOwnerId(UUID.fromString(albumId));
        if (ownerId1.toString().equals(ownerId)) {
            albumRepository.delete(UUID.fromString(albumId));
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }
}
