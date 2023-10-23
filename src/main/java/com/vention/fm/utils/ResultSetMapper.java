package com.vention.fm.utils;

import com.vention.fm.domain.model.BaseModel;
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

    public static PlaylistRating mapPlaylistRating(ResultSet resultSet) {
        try {
            PlaylistRating rating = PlaylistRating.builder()
                    .userId((UUID) resultSet.getObject("user_id"))
                    .playlistId((UUID) resultSet.getObject("playlist_id"))
                    .isLiked(resultSet.getBoolean("is_liked"))
                    .build();
            map(resultSet, rating);
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
        map(resultSet, album);
        return album;
    }

    public static AlbumTracks mapAlbumTracks(ResultSet resultSet) throws SQLException {
        AlbumTracks albumTracks = AlbumTracks.builder()
                .albumId((UUID) resultSet.getObject("album_id"))
                .trackId((UUID) resultSet.getObject("track_id"))
                .trackPosition(resultSet.getInt("track_position"))
                .build();
        map(resultSet, albumTracks);
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
            map(resultSet, artist);
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
        map(resultSet, playlist);
        return playlist;
    }

    public static PlaylistTracks mapPlaylistTracks(ResultSet resultSet) throws SQLException {
        PlaylistTracks playlistTracks = PlaylistTracks.builder()
                .playlistId((UUID) resultSet.getObject("playlist_id"))
                .trackId((UUID) resultSet.getObject("track_id"))
                .trackPosition(resultSet.getInt("track_position"))
                .build();
        map(resultSet, playlistTracks);
        return playlistTracks;
    }

    public static UserEntity mapUser(ResultSet resultSet) {
        try {
            UserEntity userEntity = new UserEntity()
                    .setUsername(resultSet.getString("username"))
                    .setEmail(resultSet.getString("email"))
                    .setIsVerified(resultSet.getBoolean("is_verified"))
                    .setPassword(resultSet.getString("password"));
            userEntity.setRole(UserRole.valueOf(resultSet.getString("role")));
            map(resultSet, userEntity);
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
            map(resultSet, track);
            return track;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
            throw new RuntimeException(e);
        }
    }
}
