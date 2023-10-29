package com.vention.fm.service;

import com.vention.fm.domain.dto.album.AlbumAddTrackDto;
import com.vention.fm.domain.dto.album.AlbumDto;
import com.vention.fm.domain.dto.album.AlbumRemoveTrackDto;
import com.vention.fm.domain.dto.track.TrackDto;
import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.exception.AccessRestrictedException;
import com.vention.fm.repository.album_tracks.AlbumTracksRepository;
import com.vention.fm.repository.album_tracks.AlbumTracksRepositoryImpl;
import com.vention.fm.utils.MapStruct;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AlbumTracksService {
    private final AlbumTracksRepository albumTracksRepository = new AlbumTracksRepositoryImpl();
    private final AlbumService albumService = new AlbumService();
    private final ArtistService artistService = new ArtistService();
    private final TrackService trackService = new TrackService();
    private final MapStruct mapStruct = MapStruct.INSTANCE;

    //In this method I used DTOs to avoid sending multiple request to database as I need ids and is_blocked states
    public String save(AlbumAddTrackDto trackDto) throws AccessRestrictedException {
        Track trackState = trackService.getTrackState(trackDto.getTrackName());
        if (trackState.getIsBlocked()) {
            throw new AccessRestrictedException("Blocked tracks cannot be added into album");
        }

        Album albumState = albumService.getAlbumState(trackDto.getAlbumName(), trackDto.getUserId());
        boolean isArtistBlocked = artistService.isBlocked(trackState.getArtist().getId());
        if (isArtistBlocked) {
            throw new AccessRestrictedException("Blocked artist cannot be added into album");
        }

        int count = albumTracksRepository.getCount(albumState.getId());
        if (trackState.getArtist().getId().equals(albumState.getArtist().getId()) && count < 10) {
            albumTracksRepository.save(
                    AlbumTracks.builder()
                            .album(albumState)
                            .track(trackState)
                            .trackPosition(count + 1)
                            .build());
        } else {
            throw new AccessRestrictedException("Album should have tracks from one artist and no more than 10");
        }
        return "Track with name " + trackDto.getTrackName() + " successfully added to album " + trackDto.getAlbumName();
    }

    public AlbumDto getAlbum(String albumName, UUID ownerId) {
        Album album = albumService.getAlbum(albumName, ownerId);
        AlbumDto albumDto = mapStruct.albumToDto(album);

        List<TrackDto> albumTracks = getAlbumTracks(album.getId());
        albumDto.setTracks(albumTracks);
        return albumDto;
    }

    public List<TrackDto> getAlbumTracks(UUID albumId) {
        List<AlbumTracks> albumTracks = albumTracksRepository.getAlbumTracks(albumId);
        if (albumTracks != null) {
            List<TrackDto> trackDtoList = new LinkedList<>();
            for (AlbumTracks albumTrack : albumTracks) {
                TrackDto trackDto = mapStruct.trackToDto(albumTrack.getTrack());
                trackDto.setPosition(albumTrack.getTrackPosition());
                trackDtoList.add(trackDto);
            }
            return trackDtoList;
        } else {
            return new ArrayList<>();
        }
    }

    public String removeTrack(AlbumRemoveTrackDto albumTrackDto) {
        Album albumState = albumService.getAlbumState(albumTrackDto.getAlbumName(), albumTrackDto.getUserId());
        Track trackState = trackService.getTrackState(albumTrackDto.getTrackName());

        List<AlbumTracks> albumTracksToReorder = albumTracksRepository.getAlbumTracksToReorder(albumState.getId(), trackState.getId());
        albumTracksRepository.removeTrack(albumState.getId(), trackState.getId());
        if (!albumTracksToReorder.isEmpty()) {
            for (AlbumTracks albumTrack : albumTracksToReorder) {
                albumTracksRepository.updatePosition(albumTrack.getId(), albumTrack.getTrackPosition() - 1);
            }
        }
        return albumTrackDto.getTrackName() + " removed from album " + albumTrackDto.getAlbumName();
    }
}
