package com.vention.fm.filter;

import com.vention.fm.service.UserService;

import java.nio.file.AccessDeniedException;
import java.util.Objects;
import java.util.UUID;

public class AdminVerifier {
    private static final UserService userService = new UserService();

    public static void verifyAdmin(UUID adminId) throws AccessDeniedException {
        String userRole = userService.getUserRole(adminId);
        if (!Objects.equals(userRole, "ADMINISTRATOR")) throw new AccessDeniedException("Access denied");
    }
}
