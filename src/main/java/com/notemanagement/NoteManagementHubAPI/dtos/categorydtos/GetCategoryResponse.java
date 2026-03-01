package com.notemanagement.NoteManagementHubAPI.dtos.categorydtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetCategoryResponse(
    UUID id,
    String name,
    String iconIdentifier,
    LocalDateTime createdAt,
    int noteCount
) {}
