package com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record NoteRevisionDetailResponse(
   UUID id,
   int version,
   Map<String, Object> contentSnapshot,
   LocalDateTime createdAt
) {}
