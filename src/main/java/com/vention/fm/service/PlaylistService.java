package com.vention.fm.service;

import com.vention.fm.domain.dto.playlist.PlaylistCreateDto;
import com.vention.fm.domain.dto.playlist.PlaylistUpdateDto;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.repository.playlist.PlaylistRepository;
import com.vention.fm.repository.playlist.PlaylistRepositoryImpl;
import com.vention.fm.utils.Utils;
import org.modelmapper.ModelMapper;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PlaylistService {
    private final PlaylistRepository playlistRepository = new PlaylistRepositoryImpl();
    private final UserService userService = new UserService();
    private final PlaylistRatingService playlistRatingService = new PlaylistRatingService();
    private final ModelMapper modelMapper = Utils.modelMapper();

    public Playlist getByName(String name, UUID userId) {
        Playlist playlistByName = playlistRepository.getPlaylistByName(name);
        if (playlistByName.getOwnerId().equals(userId)) {
            return playlistByName;
        } else if (playlistByName.getIsPublic()) {
            return playlistByName;
        } else {
            throw new DataNotFoundException("Playlist " + name + " not found");
        }
    }

    public List<Playlist> getAvailablePlaylists() {
        return playlistRepository.getAvailablePlaylists();
    }

    public List<Playlist> getAllByOwnerId(UUID ownerId) {
        return playlistRepository.getAllByOwnerId(ownerId);
    }

    public void save(PlaylistCreateDto playlistDto) throws AccessDeniedException {
        UUID ownerId = playlistDto.getOwnerId();
        Boolean isBlocked = userService.isBlocked(ownerId);
        if (isBlocked) throw new AccessDeniedException("Blocked users are not allowed to create a new playlist");
        Playlist playlist = Playlist.builder()
                .name(playlistDto.getName())
                .isPublic(playlistDto.getIsPublic())
                .ownerId(playlistDto.getOwnerId())
                .build();
        playlist.setLikeCount(0);
        playlist.setDislikeCount(0);
        playlistRepository.save(playlist);
    }

    /**
     * this method updates playlist's rating columns, whenever the playlist is rated, this method is called
     *
     * @param playlistId - the playlist
     */
    public void rate(UUID playlistId) {
        int likeCount = playlistRatingService.getLikeCount(playlistId);
        int dislikeCount = playlistRatingService.getDislikeCount(playlistId);
        playlistRepository.rate(likeCount, dislikeCount, playlistId);
    }

    public void delete(UUID playlistId, UUID ownerId) throws AccessDeniedException {
        Playlist playlistById = playlistRepository.getPlaylistById(playlistId);
        if (playlistById.getOwnerId().equals(ownerId)) {
            playlistRepository.delete(playlistById.getId());
        } else {
            throw new AccessDeniedException("You do not have access to DeleteAlbumServlet this playlist");
        }
    }

    public void update(PlaylistUpdateDto playlistDto) throws AccessDeniedException {
        Playlist playlistById = playlistRepository.getPlaylistById(playlistDto.getId());
        if (!playlistById.getOwnerId().equals(playlistDto.getOwnerId()))
            throw new AccessDeniedException("You do not have access to update this playlist");
        modelMapper.map(playlistDto, playlistById);
        playlistById.setUpdatedDate(LocalDateTime.now());
        playlistRepository.update(playlistById);
    }

    public Playlist getPlaylistById(UUID playlistId) {
        return playlistRepository.getPlaylistById(playlistId);
    }
}
