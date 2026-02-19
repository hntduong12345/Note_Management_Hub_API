package com.notemanagement.NoteManagementHubAPI.dtos.authdtos;

import java.util.UUID;

public record AuthResponse(
        String status,
        String jwtToken,
        String mfaToken,
        UUID userId,
        String email
) {}
