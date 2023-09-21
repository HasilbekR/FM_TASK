package org.example.domain.model;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artist extends BaseModel{
    private String name;
    private String url;
    private Integer playCount;
    private Integer listeners;

    public static Artist map(ResultSet resultSet) {
        try {
            Artist artist = Artist.builder()
                    .name(resultSet.getString("name"))
                    .url(resultSet.getString("url"))
                    .playCount(resultSet.getInt("playCount"))
                    .listeners(resultSet.getInt("listeners"))
                    .build();
            artist.setId(UUID.fromString(resultSet.getString("id")));
            return artist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
