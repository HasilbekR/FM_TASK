package com.vention.fm.repository.album_tracks;

import com.vention.fm.domain.model.album.AlbumTracks;

import java.util.List;
import java.util.UUID;

public interface AlbumTracksRepository {
    String INSERT = "insert into album_tracks(id, created_date, updated_date, is_blocked, album_id, track_id, track_position) values(?, ?, ?, ?, ?, ?, ?)";
    String GET_ALBUM_TRACKS = "select * from album_tracks where album_id = ? order by track_position asc";
    String GET_TRACKS_FOR_REORDER = "select * from album_tracks where track_position > (select track_position from album_tracks where album_id = ? and track_id = ?) and album_id = ? order by track_position asc";
    String GET_COUNT = "select count(*) from album_tracks where album_id = ?";
    String UPDATE_POSITION = "update album_tracks set updated_date = ?, track_position = ? where id = ?";
    String REMOVE_TRACK = "delete from album_tracks where album_id = ? and track_id = ?";


    void save(AlbumTracks albumTracks);

    List<AlbumTracks> getAlbumTracks(UUID albumId);

    List<AlbumTracks> getAlbumTracksToReorder(UUID albumId, UUID trackId);

    int getCount(UUID albumId);

    void updatePosition(UUID id, int position);

    void removeTrack(UUID albumId, UUID trackId);
}
