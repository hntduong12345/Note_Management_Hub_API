package com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record NoteLinkResponse(
  UUID id,
  UUID sourceId,
  UUID targetId,
  String linkContext,
  LocalDateTime createdAt,
  String actionNoteTitle //Title base on the get action (Target/Source Note)
) {}
