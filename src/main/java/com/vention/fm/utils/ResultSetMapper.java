package com.vention.fm.utils;

import com.vention.fm.domain.model.album.Album;
import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.domain.model.playlist.PlaylistTracks;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.domain.model.user.UserEntity;
import com.vention.fm.domain.model.user.UserRole;
import com.vention.fm.repository.artist.ArtistRepository;
import com.vention.fm.repository.artist.ArtistRepositoryImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class ResultSetMapper {
    private final static ArtistRepository artistRepository = new ArtistRepositoryImpl();
    public static PlaylistRating mapPlaylistRating(ResultSet resultSet){
        try {
            PlaylistRating rating = PlaylistRating.builder()
                    .userId((UUID) resultSet.getObject("user_id"))
                    .playlistId((UUID) resultSet.getObject("playlist_id"))
                    .isLiked(resultSet.getBoolean("is_liked"))
                    .build();
            Timestamp createdDate = resultSet.getTimestamp("created_date");
            rating.setCreatedDate(createdDate.toLocalDateTime());
            Timestamp updatedDate = resultSet.getTimestamp("updated_date");
            rating.setUpdatedDate(updatedDate.toLocalDateTime());
            rating.setIsBlocked(resultSet.getBoolean("is_blocked"));
            rating.setId(UUID.fromString(resultSet.getString("id")));
            return rating;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Album mapAlbum(ResultSet resultSet) throws SQLException {
        Album album = Album.builder()
                .name(resultSet.getString("name"))
                .artistId((UUID) resultSet.getObject("artist_id"))
                .ownerId((UUID) resultSet.getObject("owner_id"))
                .build();
        Timestamp createdDate = resultSet.getTimestamp("created_date");
        album.setCreatedDate(createdDate.toLocalDateTime());
        Timestamp updatedDate = resultSet.getTimestamp("updated_date");
        album.setUpdatedDate(updatedDate.toLocalDateTime());
        album.setIsBlocked(resultSet.getBoolean("is_blocked"));
        album.setId((UUID) resultSet.getObject("id"));
        return album;
    }
    public static AlbumTracks mapAlbumTracks(ResultSet resultSet) throws SQLException {
        AlbumTracks albumTracks = AlbumTracks.builder()
                .albumId((UUID) resultSet.getObject("album_id"))
                .trackId((UUID) resultSet.getObject("track_id"))
                .trackPosition(resultSet.getInt("track_position"))
                .build();
        Timestamp createdDate = resultSet.getTimestamp("created_date");
        albumTracks.setCreatedDate(createdDate.toLocalDateTime());
        Timestamp updatedDate = resultSet.getTimestamp("updated_date");
        albumTracks.setUpdatedDate(updatedDate.toLocalDateTime());
        albumTracks.setIsBlocked(resultSet.getBoolean("is_blocked"));
        albumTracks.setId((UUID) resultSet.getObject("id"));
        return albumTracks;
    }

    public static Artist mapArtist(ResultSet resultSet) {
        try {
            Artist artist = Artist.builder()
                    .name(resultSet.getString("name"))
                    .url(resultSet.getString("url"))
                    .playcount(resultSet.getInt("playcount"))
                    .listeners(resultSet.getInt("listeners"))
                    .build();
            Timestamp createdDate = resultSet.getTimestamp("created_date");
            artist.setCreatedDate(createdDate.toLocalDateTime());
            Timestamp updatedDate = resultSet.getTimestamp("updated_date");
            artist.setUpdatedDate(updatedDate.toLocalDateTime());
            artist.setIsBlocked(resultSet.getBoolean("is_blocked"));
            artist.setId(UUID.fromString(resultSet.getString("id")));
            return artist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Playlist mapPlaylist(ResultSet resultSet) throws SQLException {
        Playlist playlist = Playlist.builder()
                .name(resultSet.getString("name"))
                .isPublic(resultSet.getBoolean("is_public"))
                .likeCount(resultSet.getInt("like_count"))
                .dislikeCount(resultSet.getInt("dislike_count"))
                .ownerId((UUID) resultSet.getObject("owner_id"))
                .build();
        Timestamp createdDate = resultSet.getTimestamp("created_date");
        playlist.setCreatedDate(createdDate.toLocalDateTime());
        Timestamp updatedDate = resultSet.getTimestamp("updated_date");
        playlist.setUpdatedDate(updatedDate.toLocalDateTime());
        playlist.setIsBlocked(resultSet.getBoolean("is_blocked"));
        playlist.setId((UUID) resultSet.getObject("id"));
        return playlist;
    }
    public static PlaylistTracks mapPlaylistTracks(ResultSet resultSet) throws SQLException {
        PlaylistTracks playlistTracks = PlaylistTracks.builder()
                .playlistId((UUID) resultSet.getObject("playlist_id"))
                .trackId((UUID) resultSet.getObject("track_id"))
                .trackPosition(resultSet.getInt("track_position"))
                .build();
        playlistTracks.setId((UUID) resultSet.getObject("id"));
        return playlistTracks;
    }
    public static UserEntity mapUser(ResultSet resultSet) {
        try {
            UserEntity userEntity  = new UserEntity()
                    .setUsername(resultSet.getString("username"))
                    .setEmail(resultSet.getString("email"))
                    .setIsVerified(resultSet.getBoolean("is_verified"))
                    .setPassword(resultSet.getString("password"));
            Timestamp createdDate = resultSet.getTimestamp("created_date");
            userEntity.setCreatedDate(createdDate.toLocalDateTime());
            Timestamp updatedDate = resultSet.getTimestamp("updated_date");
            userEntity.setUpdatedDate(updatedDate.toLocalDateTime());
            userEntity.setIsBlocked(resultSet.getBoolean("is_blocked"));
            userEntity.setId(UUID.fromString(resultSet.getString("id")));
            userEntity.setRole(UserRole.valueOf(resultSet.getString("role")));
            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            Timestamp createdDate = resultSet.getTimestamp("created_date");
            track.setCreatedDate(createdDate.toLocalDateTime());
            Timestamp updatedDate = resultSet.getTimestamp("updated_date");
            track.setUpdatedDate(updatedDate.toLocalDateTime());
            track.setIsBlocked(resultSet.getBoolean("is_blocked"));
            track.setId(UUID.fromString(resultSet.getString("id")));
            return track;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
