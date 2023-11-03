package com.vention.fm.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.artist.ArtistDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.mapper.ArtistMapper;
import com.vention.fm.repository.artist.ArtistRepository;
import com.vention.fm.repository.artist.ArtistRepositoryImpl;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.utils.Utils;
import org.mapstruct.factory.Mappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtistService {
    private final ArtistRepository artistRepository = new ArtistRepositoryImpl();
    private final ArtistMapper mapper = Mappers.getMapper(ArtistMapper.class);
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    /**
     * First we check if the artist exists in db and if it is we update artist entity as
     * When artist is saved with track it doesn't store data about play counts and listeners,
     * and when it is saved separately we get this data
     * Then if the artist doesn't exist in db we get exception and in catch block we save them
     *
     * @return -artist entity
     */
    public ArtistDto create(ArtistDto artistDto) {
        Artist artist = Artist
                .builder()
                .name(artistDto.getName())
                .url(artistDto.getUrl())
                .build();
        Artist savedArtist = save(artist);
        return mapper.artistToDto(savedArtist);
    }

    public Artist save(Artist artist) {
        Artist artistByName = artistRepository.getArtistByName(artist.getName());
        if (artistByName == null) {
            artistRepository.save(artist);
            return artist;
        } else {
            //id has to be set from existing object as some ids are created from project not from fm chart
            artist.setId(artistByName.getId());
            artistRepository.update(artist);
            return artistRepository.getArtistByName(artist.getName());
        }
    }

    public List<ArtistDto> saveAll(List<Artist> artistList) {
        List<ArtistDto> savedArtists = new ArrayList<>();

        for (Artist artist : artistList) {
            Artist savedArtist = save(artist);
            ArtistDto artistDto = mapper.artistToDto(savedArtist);
            savedArtists.add(artistDto);
        }
        return savedArtists;
    }

    public String saveTopArtists(String page) {
        try {
            if (page == null) {
                page = "1";
            }
            String apiUrl = Utils.getLoaderURL() + "/artist/save-top-artists?page=" + page;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("service", "main");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    List<Artist> artists = objectMapper.readValue(reader, new TypeReference<>() {
                    });
                    List<ArtistDto> savedArtists = saveAll(artists);
                    return objectMapper.writeValueAsString(savedArtists);
                }
            }
            return null;
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    //for external use
    public ArtistDto getArtistDto(String name) {
        Artist artistByName = getArtistByName(name);
        return mapper.artistToDto(artistByName);
    }
    //for internal use
    public Artist getArtistByName(String name) {
        Artist artistByName = artistRepository.getArtistByName(name);
        if (artistByName != null) {
            return artistByName;
        } else {
            throw new DataNotFoundException("Artist by name " + name + " not found");
        }
    }

    public Artist getArtistState(String name) {
        Artist artistState = artistRepository.getArtistState(name);
        if (artistState != null) {
            return artistState;
        } else {
            throw new DataNotFoundException("Artist not found");
        }
    }

    public List<ArtistDto> getAll() {
        List<Artist> artists = artistRepository.getAll();
        return mapper.artistsToDto(artists);
    }

    public UUID getIdByName(String name) {
        UUID artistId = artistRepository.getIdByName(name);
        if (artistId != null) {
            return artistId;
        } else {
            throw new DataNotFoundException("Artist with name " + name + " not found");
        }
    }

    public void blockArtist(Boolean isBlocked, String artistName) {
        Artist artistState = artistRepository.getArtistState(artistName);
        if (artistState != null) {
            artistRepository.blockArtist(isBlocked, artistName);
        } else {
            throw new DataNotFoundException("Artist with name " + artistName + " not found");
        }
    }

    public boolean isBlocked(UUID artistId) {
        return artistRepository.isBlocked(artistId);
    }
}
