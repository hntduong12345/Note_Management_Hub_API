package com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos;

import java.util.UUID;

public record AttachmentResponse(
        UUID id,
        String fileUrl,
        String fileType,
        Long fileSizeBytes
) {}
