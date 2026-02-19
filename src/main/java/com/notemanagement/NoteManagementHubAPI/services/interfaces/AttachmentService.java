package com.notemanagement.NoteManagementHubAPI.services.interfaces;

import com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos.AttachmentResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos.AttachmentUploadRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface AttachmentService {
    public CompletableFuture<AttachmentResponse> linkAttachment(AttachmentUploadRequest request, UUID userId);
    public CompletableFuture<Void> deleteAttachment(UUID id, UUID userId);
}
