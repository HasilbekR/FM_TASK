package com.vention.fm.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vention.fm.domain.model.user.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private String username;
    private String email;
    private Boolean isVerified;
    private UserRole role;
}
