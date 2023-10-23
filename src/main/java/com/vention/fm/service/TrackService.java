package com.vention.fm.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.track.TrackDto;
import com.vention.fm.domain.dto.track.TrackSaveDto;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.repository.track.TrackRepository;
import com.vention.fm.repository.track.TrackRepositoryImpl;
import com.vention.fm.utils.Utils;
import org.modelmapper.ModelMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TrackService {
    private final TrackRepository trackRepository = new TrackRepositoryImpl();
    private final ArtistService artistService = new ArtistService();
    private final ModelMapper modelMapper = Utils.modelMapper();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    public TrackDto createTrack(TrackSaveDto trackSaveDto) {
        Artist artistByName = artistService.getArtistByName(trackSaveDto.getArtist());
        if (artistByName == null) {
            throw new DataNotFoundException("Artist by name " + trackSaveDto.getArtist() + " not found");
        }
        Track track = Track.builder()
                .name(trackSaveDto.getName())
                .url(trackSaveDto.getUrl())
                .duration(trackSaveDto.getDuration())
                .listeners(0)
                .playcount(0)
                .artist(artistByName)
                .build();
        save(track);
        return modelMapper.map(track, TrackDto.class);
    }

    public Track save(Track track) {
        Artist artist = track.getArtist();
        artist = artistService.save(artist);

        track.setArtist(artist);
        Track trackByNameAndArtist = trackRepository.getTrackByNameAndArtist(track.getName(), track.getArtist().getId());

        if (trackByNameAndArtist != null) {
            track.setId(trackByNameAndArtist.getId());
            trackRepository.update(track);
            return trackRepository.getTrackByNameAndArtist(track.getName(), track.getArtist().getId());
        } else {
            trackRepository.save(track);
            return track;
        }
    }

    public List<TrackDto> saveAll(List<Track> trackList) {
        List<Track> tracks = new ArrayList<>();
        for (Track track : trackList) {
            tracks.add(save(track));
        }
        return mapToDtoList(tracks);
    }

    public String saveTopTracks(String page) throws IOException {
        if (page == null) {
            page = "1";
        }
        String apiUrl = Utils.url("/url") + "/track/save-top-tracks?page=" + page;
        return getTracks(apiUrl);
    }

    public String saveTopTracksByArtist(String artist, String page) throws IOException {
        if (page == null) {
            page = "1";
        }
        String encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8);
        String apiUrl = Utils.url("/url") + "/track/save-top-tracks-by-artist?artist=" + encodedArtist + "&page=" + page;
        return getTracks(apiUrl);
    }

    public TrackDto getTrackByName(String name) {
        if (name == null) {
            throw new DataNotFoundException("Please provide the track name");
        }
        Track track = trackRepository.getTrackByName(name);
        if (track != null) {
            return mapToDto(track);
        } else {
            throw new DataNotFoundException("Track with name " + name + " not found");
        }
    }

    public Track getTrackState(String trackName) {
        Track trackState = trackRepository.getTrackState(trackName);
        if (trackState != null) {
            return trackState;
        } else {
            throw new DataNotFoundException("Track not found");
        }
    }

    public List<TrackDto> getAll() {
        return mapToDtoList(trackRepository.getAll());
    }

    public List<TrackDto> getTrackListByArtistName(String name) {
        if (name == null) {
            throw new DataNotFoundException("Please provide the artist name");
        }
        UUID artistId = artistService.getIdByName(name);
        List<Track> tracks = trackRepository.getTrackListByArtist(artistId);
        return mapToDtoList(tracks);
    }

    public List<TrackDto> searchTracksByName(String name) {
        List<Track> tracks = trackRepository.searchTracksByName(name);
        return mapToDtoList(tracks);
    }

    public void blockTrack(Boolean isBlocked, String trackName) {
        Track trackState = trackRepository.getTrackState(trackName);
        if (trackState != null) {
            trackRepository.blockTrack(isBlocked, trackName);
        } else {
            throw new DataNotFoundException("Track with name " + trackName + " not found");
        }
    }

    private String getTracks(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("service", "main");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                List<Track> tracks = objectMapper.readValue(reader, new TypeReference<>() {
                });
                return objectMapper.writeValueAsString(saveAll(tracks));
            }
        }
        return null;
    }

    private List<TrackDto> mapToDtoList(List<Track> tracks) {
        List<TrackDto> trackDtoList = new LinkedList<>();
        for (Track track : tracks) {
            trackDtoList.add(mapToDto(track));
        }
        return trackDtoList;
    }

    private TrackDto mapToDto(Track track) {
        return modelMapper.map(track, TrackDto.class);
    }
}
