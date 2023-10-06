package com.vention.fm.domain.dto.user;

import lombok.Data;

import java.util.UUID;
@Data
public class UserBlockDto {
    private UUID userId;
    private UUID adminId;
}
