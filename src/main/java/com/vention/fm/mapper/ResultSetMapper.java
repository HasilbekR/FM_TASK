package com.vention.fm.mapper;

import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.domain.model.playlist.PlaylistTracks;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.domain.model.user.User;
import com.vention.fm.domain.model.user.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ResultSetMapper {
    private static final Logger log = LoggerFactory.getLogger(ResultSetMapper.class);

    public static User mapUser(ResultSet resultSet) {
        try {
            User user = new User()
                    .setUsername(resultSet.getString("username"))
                    .setEmail(resultSet.getString("email"))
                    .setPassword(resultSet.getString("password"));
            user.setRole(UserRole.valueOf(resultSet.getString("role")));
            user.setId(UUID.fromString(resultSet.getString("user_id")));
            user.setIsBlocked(resultSet.getBoolean("user_is_blocked"));
            return user;
        } catch (SQLException e) {
            log.error("Error occurred while mapping user", e);
        }
        return null;
    }

    public static Track mapTrack(ResultSet resultSet) {
        try {
            Track track = Track.builder()
                    .name(resultSet.getString("track_name"))
                    .duration(resultSet.getInt("duration"))
                    .url(resultSet.getString("track_url"))
                    .playcount(resultSet.getInt("track_playcount"))
                    .listeners(resultSet.getInt("track_listeners"))
                    .artist(mapArtist(resultSet))
                    .build();
            track.setId(UUID.fromString(resultSet.getString("track_id")));
            track.setIsBlocked(resultSet.getBoolean("track_is_blocked"));
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
                    .name(resultSet.getString("artist_name"))
                    .url(resultSet.getString("artist_url"))
                    .playcount(resultSet.getInt("artist_playcount"))
                    .listeners(resultSet.getInt("artist_listeners"))
                    .build();
            artist.setId(UUID.fromString(resultSet.getString("artist_id")));
            artist.setIsBlocked(resultSet.getBoolean("artist_is_blocked"));
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
                    .name(resultSet.getString("album_name"))
                    .artist(mapArtist(resultSet))
                    .owner(mapUser(resultSet))
                    .build();
            album.setId(UUID.fromString(resultSet.getString("album_id")));
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
                    .track(mapTrack(resultSet))
                    .trackPosition(resultSet.getInt("track_position"))
                    .build();
            albumTracks.setId(UUID.fromString(resultSet.getString("album_track_id")));
            return albumTracks;
        } catch (SQLException e) {
            log.error("Error occurred while mapping album tracks", e);
        }
        return null;
    }

    public static Playlist mapPlaylist(ResultSet resultSet) {
        try {
            Playlist playlist = Playlist.builder()
                    .name(resultSet.getString("playlist_name"))
                    .isPublic(resultSet.getBoolean("is_public"))
                    .likeCount(resultSet.getInt("like_count"))
                    .dislikeCount(resultSet.getInt("dislike_count"))
                    .owner(mapUser(resultSet))
                    .build();
            playlist.setId(UUID.fromString(resultSet.getString("playlist_id")));
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
                    .track(mapTrack(resultSet))
                    .trackPosition(resultSet.getInt("track_position"))
                    .build();
            playlistTracks.setId(UUID.fromString(resultSet.getString("playlist_track_id")));
            return playlistTracks;
        } catch (SQLException e) {
            log.error("Error occurred while mapping playlist tracks", e);
        }
        return null;
    }

    public static PlaylistRating mapPlaylistRating(ResultSet resultSet) {
        try {
            PlaylistRating rating = PlaylistRating.builder()
                    .isLiked(resultSet.getBoolean("is_liked"))
                    .build();
            rating.setId(UUID.fromString(resultSet.getString("playlist_rating_id")));
            return rating;
        } catch (SQLException e) {
            log.error("Error occurred while mapping playlist rating", e);
        }
        return null;
    }
}
