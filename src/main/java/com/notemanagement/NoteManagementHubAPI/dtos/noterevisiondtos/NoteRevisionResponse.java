package com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record NoteRevisionResponse(
        UUID id,
        int version,
        LocalDateTime createdAt
) {}
