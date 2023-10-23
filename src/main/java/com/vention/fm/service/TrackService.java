package com.vention.fm.service;

import com.vention.fm.domain.dto.track.TrackSaveDto;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.repository.track.TrackRepository;
import com.vention.fm.repository.track.TrackRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackService {
    private final TrackRepository trackRepository = new TrackRepositoryImpl();
    private final ArtistService artistService = new ArtistService();

    public List<Track> searchTracksByName(String name) {
        return trackRepository.searchTracksByName(name);
    }

    public Track getTrackByName(String name) {
        if (name == null) throw new DataNotFoundException("Please provide the track name");
        return trackRepository.getTrackByName(name);
    }

    public List<Track> getTrackListByArtistName(String name) {
        if (name == null) throw new DataNotFoundException("Please provide the artist name");
        UUID artistId = artistService.getIdByName(name);
        return trackRepository.getTrackListByArtist(artistId);
    }

    public List<Track> getAll() {
        return trackRepository.getAll();
    }

    public Track save(Track track) {
        Artist artist = track.getArtist();
        artist = artistService.save(artist);

        track.setArtist(artist);
        try {
            Track trackByNameAndArtist = trackRepository.getTrackByNameAndArtist(track.getName(), track.getArtist().getId());
            track.setId(trackByNameAndArtist.getId());
            trackRepository.update(track);
            return trackRepository.getTrackByNameAndArtist(track.getName(), track.getArtist().getId());
        } catch (DataNotFoundException e) {
            trackRepository.save(track);
            return trackRepository.getTrackByNameAndArtist(track.getName(), track.getArtist().getId());
        }
    }

    public Track createTrack(TrackSaveDto trackSaveDto) {
        Track track = Track.builder()
                .name(trackSaveDto.getName())
                .url(trackSaveDto.getUrl())
                .duration(trackSaveDto.getDuration())
                .listeners(0)
                .playcount(0)
                .artist(artistService.getArtistByName(trackSaveDto.getArtist()))
                .build();
        return save(track);
    }

    public List<Track> saveAll(List<Track> trackList) {
        List<Track> tracks = new ArrayList<>();
        for (Track track : trackList) {
            tracks.add(save(track));
        }
        return tracks;
    }

    public Boolean isBlocked(UUID trackId) {
        return trackRepository.isBlocked(trackId);
    }

    public UUID getArtistId(UUID trackId) {
        return trackRepository.getArtistId(trackId);
    }

    public void blockTrack(Boolean isBlocked, UUID trackId) {
        trackRepository.blockTrack(isBlocked, trackId);
    }
}
