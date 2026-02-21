package com.notemanagement.NoteManagementHubAPI.services.interfaces;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionDetailResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionResponse;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NoteRevisionService {
    CompletableFuture<Void> createRevision(UUID noteId, NoteRevisionRequest request, UUID userId);
    CompletableFuture<List<NoteRevisionResponse>> getRevisionHistory(UUID noteId, UUID userId);
    CompletableFuture<NoteRevisionDetailResponse> getRevisionDetail(UUID revisionId, UUID userId);
    CompletableFuture<NoteResponse> restoreRevision(UUID revisionId, UUID userId);
}
