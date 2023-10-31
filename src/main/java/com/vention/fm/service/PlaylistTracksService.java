package com.vention.fm.service;

import com.vention.fm.domain.dto.playlist.PlaylistRequestDto;
import com.vention.fm.domain.dto.playlist.PlaylistResponseDto;
import com.vention.fm.domain.dto.track.TrackDto;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistTracks;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.exception.AccessRestrictedException;
import com.vention.fm.exception.AuthenticationFailedException;
import com.vention.fm.mapper.PlaylistMapper;
import com.vention.fm.mapper.TrackMapper;
import com.vention.fm.repository.playlist_tracks.PlaylistTracksRepository;
import com.vention.fm.repository.playlist_tracks.PlaylistTracksRepositoryImpl;
import org.mapstruct.factory.Mappers;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class PlaylistTracksService {
    private final PlaylistTracksRepository playlistTracksRepository = new PlaylistTracksRepositoryImpl();
    private final PlaylistService playlistService = new PlaylistService();
    private final TrackService trackService = new TrackService();
    private final PlaylistMapper playlistMapper = Mappers.getMapper(PlaylistMapper.class);
    private final TrackMapper trackMapper = Mappers.getMapper(TrackMapper.class);

    public String addPlaylist(PlaylistRequestDto trackDto) throws AccessRestrictedException {
        Track trackState = trackService.getTrackState(trackDto.getTrackName());

        if (trackState.getIsBlocked()) {
            throw new AccessRestrictedException("Blocked tracks cannot be added into playlist");
        }

        Playlist playlistState = playlistService.getPlaylistState(trackDto.getName());
        if (playlistState != null) {
            if (playlistState.getOwner().getId().equals(trackDto.getUserId())) {
                int position = playlistTracksRepository.countPlaylistTracks(playlistState.getId()) + 1;
                PlaylistTracks playlistTracks = new PlaylistTracks(playlistState, trackState, position);
                playlistTracksRepository.save(playlistTracks);
            } else {
                throw new AuthenticationFailedException("Access denied to the playlist");
            }
        }
        return trackDto.getTrackName() + " successfully added to playlist " + trackDto.getName();
    }

    public PlaylistResponseDto getPlaylist(String playlistName, UUID ownerId) {
        Playlist playlist = playlistService.getByName(playlistName, ownerId);
        List<PlaylistTracks> playlistTracks = playlistTracksRepository.getPlaylistTracks(playlist.getId());
        List<TrackDto> tracks = new LinkedList<>();
        if(!playlistTracks.isEmpty()) {
            for (PlaylistTracks playlistTrack : playlistTracks) {
                Track track = playlistTrack.getTrack();
                TrackDto trackDto = trackMapper.trackToDto(track);
                trackDto.setPosition(playlistTrack.getTrackPosition());

                tracks.add(trackDto);
            }
        }
        PlaylistResponseDto playlistResponseDto = playlistMapper.playlistToDto(playlist);
        playlistResponseDto.setTracks(tracks);
        return playlistResponseDto;
    }

    public String removeTrack(PlaylistRequestDto playlistTrackDto) {
        Playlist playlistState = playlistService.getPlaylistState(playlistTrackDto.getName());
        Track trackState = trackService.getTrackState(playlistTrackDto.getTrackName());

        if (playlistState.getOwner().getId().equals(playlistTrackDto.getUserId())) {
            List<PlaylistTracks> playlistTracksToReorder = playlistTracksRepository.getPlaylistTracksToReorder(playlistState.getId(), trackState.getId());
            playlistTracksRepository.removeTrack(playlistState.getId(), trackState.getId());
            if (!playlistTracksToReorder.isEmpty()) {
                for (PlaylistTracks playlistTrack : playlistTracksToReorder) {
                    playlistTracksRepository.updatePosition(playlistTrack.getId(), playlistTrack.getTrackPosition() - 1);
                }
            }
        } else {
            throw new AuthenticationFailedException("Access denied to the playlist");
        }
        return playlistTrackDto.getTrackName() + " removed from playlist " + playlistTrackDto.getName();
    }
}
