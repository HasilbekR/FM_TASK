package com.vention.fm.service;

import com.vention.fm.domain.dto.playlist.*;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.domain.model.user.User;
import com.vention.fm.exception.AccessRestrictedException;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.mapper.PlaylistMapper;
import com.vention.fm.repository.playlist.PlaylistRepository;
import com.vention.fm.repository.playlist.PlaylistRepositoryImpl;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

public class PlaylistService {
    private final PlaylistRepository playlistRepository = new PlaylistRepositoryImpl();
    private final UserService userService = new UserService();
    private final PlaylistRatingService playlistRatingService = new PlaylistRatingService();
    private final PlaylistMapper mapper = Mappers.getMapper(PlaylistMapper.class);

    public String save(PlaylistRequestDto playlistDto) throws AccessRestrictedException {
        User user = userService.getUserState(playlistDto.getUserId());
        if (user.getIsBlocked()) {
            throw new AccessRestrictedException("Blocked users are not allowed to create a new playlist");
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

    public List<PlaylistResponseDto> getAvailablePlaylists() {
        List<Playlist> availablePlaylists = playlistRepository.getAvailablePlaylists();
        return mapper.playlistsToDto(availablePlaylists);
    }

    public List<PlaylistResponseDto> getAllByOwnerId(UUID ownerId) {
        List<Playlist> playlists = playlistRepository.getAllByOwnerId(ownerId);
        return mapper.playlistsToDto(playlists);
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

    public String update(PlaylistRequestDto playlistResponseDto) throws AccessRestrictedException {
        Playlist playlist = playlistRepository.getPlaylistByName(playlistResponseDto.getName());
        if (playlist != null) {
            if (!playlist.getOwner().getId().equals(playlistResponseDto.getUserId())) {
                throw new AccessRestrictedException("You do not have access to update this playlist");
            }
            if(playlistResponseDto.getIsPublic() != null){
                playlist.setIsPublic(playlistResponseDto.getIsPublic());
            }
            if(playlistResponseDto.getUpdatedName() != null){
                playlist.setName(playlistResponseDto.getUpdatedName());
            }
            playlistRepository.update(playlist);
            return "Playlist successfully updated";
        } else {
            throw new DataNotFoundException("Playlist with name " + playlistResponseDto.getName() + " not found");
        }
    }

    public String delete(String playlistName, UUID ownerId) throws AccessRestrictedException {
        Playlist playlistByName = playlistRepository.getPlaylistByName(playlistName);
        if (playlistByName != null) {
            if (playlistByName.getOwner().getId().equals(ownerId)) {
                playlistRepository.delete(playlistByName.getId());
                return "Playlist deleted successfully";
            } else {
                throw new AccessRestrictedException("You do not have access to delete this playlist");
            }
        } else {
            throw new DataNotFoundException("Playlist with name " + playlistName + " not found");
        }
    }

    public String ratePlaylist(PlaylistRequestDto playlistRatingDto) {
        Playlist playlist = getPlaylistState(playlistRatingDto.getName());
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
            throw new DataNotFoundException("Playlist with name " + playlistRatingDto.getName() + " not found");
        }
    }
}
