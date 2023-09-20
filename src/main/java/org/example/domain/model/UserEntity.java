package org.example.domain.model;

import lombok.*;
import lombok.experimental.Accessors;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseModel{
    private String username;
    private String email;
    private String password;
    private Boolean isVerified;
    public static UserEntity map(ResultSet resultSet) {
        try {
            UserEntity userEntity  = new UserEntity()
                    .setUsername(resultSet.getString("username"))
                    .setEmail(resultSet.getString("email"))
                    .setPassword(resultSet.getString("password"));
            userEntity.setId(UUID.fromString(resultSet.getString("id")));
            return userEntity;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id + '\''+
                ", createdDate=" + createdDate + '\''+
                ", updatedDate=" + updatedDate + '\''+
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isVerified=" + isVerified +
                '}';
    }
}
