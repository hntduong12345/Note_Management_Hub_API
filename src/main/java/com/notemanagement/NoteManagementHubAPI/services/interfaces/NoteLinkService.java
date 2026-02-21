package com.notemanagement.NoteManagementHubAPI.services.interfaces;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos.NoteLinkRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos.NoteLinkResponse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NoteLinkService {
    CompletableFuture<NoteLinkResponse> createLink(NoteLinkRequest request);
    CompletableFuture<Void> removeLink(UUID linkId, UUID userId);
    CompletableFuture<List<NoteLinkResponse>> getTargetLinks(UUID noteId, UUID userId);
    CompletableFuture<List<NoteLinkResponse>> getSourceLinks(UUID noteId, UUID userId);
}
