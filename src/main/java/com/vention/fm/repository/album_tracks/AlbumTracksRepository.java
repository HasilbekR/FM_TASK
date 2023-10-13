package com.vention.fm.repository.album_tracks;

import com.vention.fm.domain.model.album.AlbumTracks;

import java.util.List;
import java.util.UUID;

public interface AlbumTracksRepository {
    String GET_ALBUM_TRACKS = "select * from album_tracks where album_id = ? order by track_position asc";
    String INSERT = "insert into album_tracks(id, created_date, updated_date, is_blocked, album_id, track_id, track_position) values(?,?,?,?,?,?,?)";
    String GET_COUNT = "select count(*) from album_tracks where album_id =?";

    void save(AlbumTracks albumTracks);

    List<AlbumTracks> getAlbumTracks(UUID albumId);

    int getCount(UUID albumId);
}
