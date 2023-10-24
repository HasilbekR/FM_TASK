package com.vention.fm.service;

import com.vention.fm.domain.dto.playlist.*;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.domain.model.user.User;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.repository.playlist.PlaylistRepository;
import com.vention.fm.repository.playlist.PlaylistRepositoryImpl;
import com.vention.fm.utils.MapStruct;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public class PlaylistService {
    private final PlaylistRepository playlistRepository = new PlaylistRepositoryImpl();
    private final UserService userService = new UserService();
    private final PlaylistRatingService playlistRatingService = new PlaylistRatingService();
    private final MapStruct mapStruct = MapStruct.INSTANCE;

    public String save(PlaylistCreateDto playlistDto) throws AccessDeniedException {
        User user = userService.getUserState(playlistDto.getUserId());
        if (user.getIsBlocked()) {
            throw new AccessDeniedException("Blocked users are not allowed to create a new playlist");
        }
        Playlist playlist = Playlist.builder()
                .name(playlistDto.getName())
                .isPublic(playlistDto.getIsPublic())
                .owner(user)
                .likeCount(0)
                .dislikeCount(0)
                .build();
        playlistRepository.save(playlist);
        return "Playlist created successfully";
    }

    public Playlist getByName(String name, UUID userId) {
        Playlist playlistByName = playlistRepository.getPlaylistByName(name);
        if (playlistByName != null) {
            if (playlistByName.getOwner().getId().equals(userId) || playlistByName.getIsPublic()) {
                return playlistByName;
            } else {
                throw new DataNotFoundException("Playlist " + name + " not found");
            }
        } else {
            throw new DataNotFoundException("Playlist with name " + name + " not found");
        }
    }

    public Playlist getPlaylistState(String name) {
        Playlist playlistState = playlistRepository.getPlaylistState(name);
        if (playlistState != null) {
            return playlistState;
        } else {
            throw new DataNotFoundException("Playlist with name " + name + " not found");
        }
    }

    public List<PlaylistDto> getAvailablePlaylists() {
        List<Playlist> availablePlaylists = playlistRepository.getAvailablePlaylists();
        return mapStruct.playlistsToDto(availablePlaylists);
    }

    public List<PlaylistDto> getAllByOwnerId(UUID ownerId) {
        List<Playlist> playlists = playlistRepository.getAllByOwnerId(ownerId);
        return mapStruct.playlistsToDto(playlists);
    }

    /**
     * this method updates playlist's rating columns, whenever the playlist is rated, this method is called
     *
     * @param playlistId - the playlist
     */
    public void updateRating(UUID playlistId) {
        int likeCount = playlistRatingService.getLikeCount(playlistId);
        int dislikeCount = playlistRatingService.getDislikeCount(playlistId);
        playlistRepository.rate(likeCount, dislikeCount, playlistId);
    }

    public String update(PlaylistUpdateDto playlistDto) throws AccessDeniedException {
        Playlist playlist = playlistRepository.getPlaylistByName(playlistDto.getName());
        if (playlist != null) {
            if (!playlist.getOwner().getId().equals(playlistDto.getUserId())) {
                throw new AccessDeniedException("You do not have access to update this playlist");
            }
            if(playlistDto.getIsPublic() != null){
                playlist.setIsPublic(playlistDto.getIsPublic());
            }
            if(playlistDto.getUpdatedName() != null){
                playlist.setName(playlistDto.getUpdatedName());
            }
            playlistRepository.update(playlist);
            return "Playlist successfully updated";
        } else {
            throw new DataNotFoundException("Playlist with name " + playlistDto.getName() + " not found");
        }
    }

    public String delete(String playlistName, UUID ownerId) throws AccessDeniedException {
        Playlist playlistByName = playlistRepository.getPlaylistByName(playlistName);
        if (playlistByName != null) {
            if (playlistByName.getOwner().getId().equals(ownerId)) {
                playlistRepository.delete(playlistByName.getId());
                return "Playlist deleted successfully";
            } else {
                throw new AccessDeniedException("You do not have access to DeleteAlbumServlet this playlist");
            }
        } else {
            throw new DataNotFoundException("Playlist with name " + playlistName + " not found");
        }
    }

    public String ratePlaylist(PlaylistRatingDto playlistRatingDto) {
        Playlist playlist = getPlaylistState(playlistRatingDto.getPlaylistName());
        PlaylistRating rating = playlistRatingService.get(playlist.getId(), playlistRatingDto.getUserId());
        // checks if the playlist available to rate
        if (playlist.getOwner().getId().equals(playlistRatingDto.getUserId()) || playlist.getIsPublic()) {
            if (rating != null) {
                // if user changes rating "like->dislike", rating is updated
                if (playlistRatingDto.getIsLiked() != rating.isLiked()) {
                    playlistRatingService.update(playlist.getId(), playlistRatingDto.getUserId(), playlistRatingDto.getIsLiked());
                    updateRating(playlist.getId());
                    return "Rating updated successfully";
                }
                // if user rates the same again(like->like), rating is deleted
                else {
                    playlistRatingService.delete(playlist.getId(), playlistRatingDto.getUserId());
                    updateRating(playlist.getId());
                    return "Rating removed successfully";
                }
            }
            // if user has not rated playlist then new rating is created
            else {
                User user = userService.getUserState(playlistRatingDto.getUserId());
                PlaylistRating playlistRating = PlaylistRating.builder()
                        .playlist(playlist)
                        .user(user)
                        .isLiked(playlistRatingDto.getIsLiked())
                        .build();
                playlistRatingService.save(playlistRating);
                updateRating(playlist.getId());
                return "Rating saved successfully";
            }
        } else {
            throw new DataNotFoundException("Playlist with name " + playlistRatingDto.getPlaylistName() + " not found");
        }
    }
}
