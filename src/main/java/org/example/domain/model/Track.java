package org.example.domain.model;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Track extends BaseModel{
    private String name;
    private String url;
    private Integer duration;
    private Integer playCount;
    private Integer listeners;
    private UUID artistId;
    public static Track map(ResultSet resultSet) {
        try {
            Track track = Track.builder()
                    .name(resultSet.getString("name"))
                    .duration(resultSet.getInt("duration"))
                    .url(resultSet.getString("url"))
                    .playCount(resultSet.getInt("playCount"))
                    .listeners(resultSet.getInt("listeners"))
                    .artistId(UUID.fromString(resultSet.getString("artistId")))
                    .build();
            track.setId(UUID.fromString(resultSet.getString("id")));
            return track;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public String toString() {
        return "Track{" +
                "id=" + id + '\''+
                ", createdDate=" + createdDate + '\''+
                ", updatedDate=" + updatedDate + '\''+
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", duration=" + duration +
                ", playCount=" + playCount +
                ", listeners=" + listeners +
                ", artist=" + artistId +
                '}';
    }

}
