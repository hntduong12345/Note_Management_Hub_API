package com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentUploadRequest {
    @NotNull(message = "Note ID is required")
    private UUID noteId;

    @NotBlank(message = "File URL is required")
    private String fileUrl;

    @NotBlank(message = "File type is required")
    private String fileType; // e.g., "image/png"

    @Positive(message = "File size must be greater than zero")
    private Long fileSizeBytes;
}
