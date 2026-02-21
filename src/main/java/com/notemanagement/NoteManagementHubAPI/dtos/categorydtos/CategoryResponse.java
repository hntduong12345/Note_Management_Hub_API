package com.notemanagement.NoteManagementHubAPI.dtos.categorydtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
    UUID id,
    String name,
    String iconIdentifier,
    LocalDateTime createdAt
){}
