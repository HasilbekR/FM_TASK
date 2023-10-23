package com.vention.fm.service;

import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.repository.artist.ArtistRepository;
import com.vention.fm.repository.artist.ArtistRepositoryImpl;
import com.vention.fm.domain.model.artist.Artist;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtistService {
    private final ArtistRepository artistRepository = new ArtistRepositoryImpl();

    public Artist getArtistByName(String name) {
        return artistRepository.getArtistByName(name);
    }

    public List<Artist> getAll() {
        return artistRepository.getAll();
    }

    /**
     * First we check if the artist exists in db and if it is we update artist entity as
     * When artist is saved with track it doesn't store data about play counts and listeners,
     * and when it is saved separately we get this data
     * Then if the artist doesn't exist in db we get exception and in catch block we save them
     *
     * @param artist -
     * @return -artist entity
     */
    public Artist save(Artist artist) {
        try {
            Artist artistByName = artistRepository.getArtistByName(artist.getName());
            //id has to be set from existing object as some ids are created from project not from fm chart
            artist.setId(artistByName.getId());
            artistRepository.update(artist);
            return artistRepository.getArtistByName(artist.getName());
        } catch (DataNotFoundException e) {
            artistRepository.save(artist);
            return artist;
        }
    }

    public List<Artist> saveAll(List<Artist> artistList) {
        List<Artist> savedArtists = new ArrayList<>();

        for (Artist artist : artistList) {
            Artist savedArtist = save(artist);
            savedArtists.add(savedArtist);
        }
        return savedArtists;
    }

    public UUID getIdByName(String name) {
        return artistRepository.getIdByName(name);
    }

    public void blockArtist(Boolean isBlocked, UUID artistId) {
        artistRepository.blockArtist(isBlocked, artistId);
    }

    public Boolean isBlocked(UUID artistId) {
        return artistRepository.isBlocked(artistId);
    }
}
