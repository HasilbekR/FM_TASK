package com.vention.fm.domain.model.user;

import com.vention.fm.domain.model.BaseModel;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseModel {
    private String username;
    private String email;
    private String password;
    private Boolean isVerified;
    private UserRole role;
}
