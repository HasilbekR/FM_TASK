package com.vention.fm.utils;

import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.domain.model.playlist.PlaylistTracks;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.domain.model.user.User;
import com.vention.fm.domain.model.user.UserRole;
import com.vention.fm.repository.album.AlbumRepository;
import com.vention.fm.repository.album.AlbumRepositoryImpl;
import com.vention.fm.repository.artist.ArtistRepository;
import com.vention.fm.repository.artist.ArtistRepositoryImpl;
import com.vention.fm.repository.playlist.PlaylistRepository;
import com.vention.fm.repository.playlist.PlaylistRepositoryImpl;
import com.vention.fm.repository.track.TrackRepository;
import com.vention.fm.repository.track.TrackRepositoryImpl;
import com.vention.fm.repository.user.UserRepository;
import com.vention.fm.repository.user.UserRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class ResultSetMapper {
    private final static ArtistRepository artistRepository = new ArtistRepositoryImpl();
    private final static UserRepository userRepository = new UserRepositoryImpl();
    private final static PlaylistRepository playlistRepository = new PlaylistRepositoryImpl();
    private final static AlbumRepository albumRepository = new AlbumRepositoryImpl();
    private final static TrackRepository trackRepository = new TrackRepositoryImpl();

    private static final Logger log = LoggerFactory.getLogger(ResultSetMapper.class);

    public static User mapUser(ResultSet resultSet) {
        try {
            User user = new User()
                    .setUsername(resultSet.getString("username"))
                    .setEmail(resultSet.getString("email"))
                    .setIsVerified(resultSet.getBoolean("is_verified"))
                    .setPassword(resultSet.getString("password"));
            user.setRole(UserRole.valueOf(resultSet.getString("role")));
            map(resultSet, user);
            return user;
        } catch (SQLException e) {
            log.error("Error occurred while mapping user", e);
        }
        return null;
    }

    public static Track mapTrack(ResultSet resultSet) {
        try {
            Track track = Track.builder()
                    .name(resultSet.getString("name"))
                    .duration(resultSet.getInt("duration"))
                    .url(resultSet.getString("url"))
                    .playcount(resultSet.getInt("playcount"))
                    .listeners(resultSet.getInt("listeners"))
                    .artist(artistRepository.getArtistById(UUID.fromString(resultSet.getString("artist_id"))))
                    .build();
            map(resultSet, track);
            return track;
        } catch (SQLException e) {
            log.error("Error occurred while mapping track", e);
        }
        return null;
    }

    public static Track mapTrackState(ResultSet resultSet) {
        try {
            Artist artist = new Artist();
            UUID artistId = (UUID) resultSet.getObject("artist_id");
            artist.setId(artistId);

            Track track = new Track();
            UUID trackId = (UUID) resultSet.getObject("id");
            track.setId(trackId);
            track.setArtist(artist);
            track.setIsBlocked(resultSet.getBoolean("is_blocked"));
            return track;
        } catch (SQLException e) {
            log.error("Error occurred while mapping track state", e);
        }
        return null;
    }

    public static Artist mapArtist(ResultSet resultSet) {
        try {
            Artist artist = Artist.builder()
                    .name(resultSet.getString("name"))
                    .url(resultSet.getString("url"))
                    .playcount(resultSet.getInt("playcount"))
                    .listeners(resultSet.getInt("listeners"))
                    .build();
            map(resultSet, artist);
            return artist;
        } catch (SQLException e) {
            log.error("Error occurred while mapping artist", e);
        }
        return null;
    }

    public static Artist mapArtistState(ResultSet resultSet) {
        try {
            Artist artist = new Artist();
            artist.setId((UUID) resultSet.getObject("id"));
            artist.setIsBlocked(resultSet.getBoolean("is_blocked"));
            return artist;
        } catch (SQLException e) {
            log.error("Error occurred while mapping artist state", e);
        }
        return null;
    }

    public static Album mapAlbum(ResultSet resultSet) {
        try {
            Album album = Album.builder()
                    .name(resultSet.getString("name"))
                    .artist(artistRepository.getArtistById(UUID.fromString(resultSet.getString("artist_id"))))
                    .owner(userRepository.getById((UUID) resultSet.getObject("owner_id")))
                    .build();
            map(resultSet, album);
            return album;
        } catch (SQLException e) {
            log.error("Error occurred while mapping album", e);
        }
        return null;
    }

    public static Album mapAlbumState(ResultSet resultSet) {
        try {
            Artist artist = new Artist();
            artist.setId((UUID) resultSet.getObject("artist_id"));
            Album album = new Album();
            album.setId((UUID) resultSet.getObject("id"));
            album.setArtist(artist);
            return album;
        } catch (SQLException e) {
            log.error("Error occurred while mapping album state", e);
        }
        return null;
    }

    public static AlbumTracks mapAlbumTracks(ResultSet resultSet) {
        try {
            AlbumTracks albumTracks = AlbumTracks.builder()
                    .album(albumRepository.getById((UUID) resultSet.getObject("album_id")))
                    .track(trackRepository.getById((UUID) resultSet.getObject("track_id")))
                    .trackPosition(resultSet.getInt("track_position"))
                    .build();
            map(resultSet, albumTracks);
            return albumTracks;
        } catch (SQLException e) {
            log.error("Error occurred while mapping album tracks", e);
        }
        return null;
    }

    public static Playlist mapPlaylist(ResultSet resultSet) {
        try {
            Playlist playlist = Playlist.builder()
                    .name(resultSet.getString("name"))
                    .isPublic(resultSet.getBoolean("is_public"))
                    .likeCount(resultSet.getInt("like_count"))
                    .dislikeCount(resultSet.getInt("dislike_count"))
                    .owner(userRepository.getById((UUID) resultSet.getObject("owner_id")))
                    .build();
            map(resultSet, playlist);
            return playlist;
        } catch (SQLException e) {
            log.error("Error occurred while mapping playlist", e);
        }
        return null;
    }

    public static Playlist mapPlaylistState(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId((UUID) resultSet.getObject("owner_id"));
            Playlist playlist = new Playlist();
            playlist.setId((UUID) resultSet.getObject("id"));
            playlist.setIsPublic(resultSet.getBoolean("is_public"));
            playlist.setOwner(user);
            return playlist;
        } catch (SQLException e) {
            log.error("Error occurred while mapping playlist state", e);
        }
        return null;
    }

    public static PlaylistTracks mapPlaylistTracks(ResultSet resultSet) {
        try {
            PlaylistTracks playlistTracks = PlaylistTracks.builder()
                    .playlist(playlistRepository.getById((UUID) resultSet.getObject("playlist_id")))
                    .track(trackRepository.getById((UUID) resultSet.getObject("track_id")))
                    .trackPosition(resultSet.getInt("track_position"))
                    .build();
            map(resultSet, playlistTracks);
            return playlistTracks;
        } catch (SQLException e) {
            log.error("Error occurred while mapping playlist tracks", e);
        }
        return null;
    }

    public static PlaylistRating mapPlaylistRating(ResultSet resultSet) {
        try {
            PlaylistRating rating = PlaylistRating.builder()
                    .user(userRepository.getById((UUID) resultSet.getObject("user_id")))
                    .playlist(playlistRepository.getById((UUID) resultSet.getObject("playlist_id")))
                    .isLiked(resultSet.getBoolean("is_liked"))
                    .build();
            map(resultSet, rating);
            return rating;
        } catch (SQLException e) {
            log.error("Error occurred while mapping playlist rating", e);
        }
        return null;
    }

    private static void map(ResultSet resultSet, BaseModel baseModel) {
        try {
            Timestamp createdDate = resultSet.getTimestamp("created_date");
            baseModel.setCreatedDate(createdDate.toLocalDateTime());
            Timestamp updatedDate = resultSet.getTimestamp("updated_date");
            baseModel.setUpdatedDate(updatedDate.toLocalDateTime());
            baseModel.setIsBlocked(resultSet.getBoolean("is_blocked"));
            baseModel.setId(UUID.fromString(resultSet.getString("id")));
        } catch (SQLException e) {
            log.error("Error occurred while mapping", e);
        }
    }
}
