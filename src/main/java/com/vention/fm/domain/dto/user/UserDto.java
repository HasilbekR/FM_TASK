package com.vention.fm.domain.dto.user;

import com.vention.fm.domain.model.user.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String email;
    private Boolean isVerified;
    private UserRole role;
}
