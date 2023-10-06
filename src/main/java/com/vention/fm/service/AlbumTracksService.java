package com.vention.fm.service;

import com.vention.fm.domain.dto.album.AlbumAddTrackDto;
import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.repository.album_tracks.AlbumTracksRepository;
import com.vention.fm.repository.album_tracks.AlbumTracksRepositoryImpl;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

public class AlbumTracksService {
    private final AlbumTracksRepository albumTracksRepository = new AlbumTracksRepositoryImpl();
    private final AlbumService albumService = new AlbumService();
    private final ArtistService artistService = new ArtistService();
    private final TrackService trackService = new TrackService();
    //User searches track and then selects it from results, therefore I used only ids of track and album
    public void save(AlbumAddTrackDto trackDto)  throws AccessDeniedException {
        Boolean isBlocked = trackService.isBlocked(trackDto.getTrackId());
        if(isBlocked) throw new AccessDeniedException("Blocked tracks cannot be added into album");

        if(!albumService.getOwnerId(trackDto.getAlbumId()).equals(trackDto.getOwnerId()))
            throw new AccessDeniedException("Access denied");

        UUID artistIdByTrack = trackService.getArtistId(trackDto.getTrackId());
        UUID artistIdByAlbum = albumService.getArtistId(trackDto.getAlbumId());

        Boolean isArtistBlocked = artistService.isBlocked(artistIdByTrack);
        if(isArtistBlocked) throw new AccessDeniedException("Blocked artist cannot be added into album");

        int count = albumTracksRepository.getCount(trackDto.getAlbumId());
        if(artistIdByTrack.equals(artistIdByAlbum) && count<10){
            albumTracksRepository.save(
                    AlbumTracks.builder()
                            .albumId(trackDto.getAlbumId())
                            .trackId(trackDto.getTrackId())
                            .trackPosition(count+1)
                            .build());
        }else {
            throw new AccessDeniedException("Album should have tracks from one artist and no more than 10");
        }
    }
    public List<AlbumTracks> getAlbumTracks(UUID albumId) {
        return albumTracksRepository.getAlbumTracks(albumId);
    }
}
