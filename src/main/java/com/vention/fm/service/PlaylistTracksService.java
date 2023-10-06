package com.vention.fm.service;

import com.vention.fm.domain.dto.playlist.PlaylistAddTrackDto;
import com.vention.fm.domain.dto.playlist.PlaylistRemoveTrackDto;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistTracks;
import com.vention.fm.exception.AuthenticationFailedException;
import com.vention.fm.repository.playlist_tracks.PlaylistTracksRepository;
import com.vention.fm.repository.playlist_tracks.PlaylistTracksRepositoryImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public class PlaylistTracksService {
    private final PlaylistTracksRepository playlistTracksRepository = new PlaylistTracksRepositoryImpl();
    private final PlaylistService playlistService = new PlaylistService();
    private final TrackService trackService = new TrackService();

    public List<PlaylistTracks> getPlaylistTracks(UUID playlistId, UUID ownerId) throws AccessDeniedException {
        Playlist playlistById = playlistService.getPlaylistById(playlistId);
        Boolean isOwner = playlistById.getOwnerId().equals(ownerId);
        if(isOwner || playlistById.getIsPublic()) return playlistTracksRepository.getPlaylistTracks(playlistId);
        throw new AccessDeniedException("Access denied");
    }
    public void addPlaylist(PlaylistAddTrackDto track) throws AccessDeniedException {
        UUID trackId = track.getTrackId();
        Boolean isBlocked = trackService.isBlocked(trackId);
        if(isBlocked) throw new AccessDeniedException("Blocked tracks cannot be added into playlist");

        Playlist playlistById = playlistService.getPlaylistById(track.getPlaylistId());
        if (playlistById.getOwnerId().equals(track.getOwnerId())) {
            int position = playlistTracksRepository.countPlaylistTracks(track.getPlaylistId()) + 1;
            PlaylistTracks playlistTracks = new PlaylistTracks(track.getPlaylistId(), track.getTrackId(), position);
            playlistTracksRepository.save(playlistTracks);
        }else {
            throw new AuthenticationFailedException("Access denied to the playlist");
        }
    }

    public void removeTrack(PlaylistRemoveTrackDto playlistTrackDto) {
        PlaylistTracks playlistTracks = playlistTracksRepository.getById(playlistTrackDto.getTrackInPlaylistId());
        Playlist playlistById = playlistService.getPlaylistById(playlistTracks.getPlaylistId());
        if (playlistById.getOwnerId().equals(playlistTrackDto.getOwnerId())) {
            playlistTracksRepository.removeTrack(playlistTrackDto.getTrackInPlaylistId());
        }else {
            throw new AuthenticationFailedException("Access denied to the playlist");
        }
    }
    public void delete(UUID playlistId){
        playlistTracksRepository.delete(playlistId);
    }
}
