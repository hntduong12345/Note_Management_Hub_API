package com.notemanagement.NoteManagementHubAPI.services.interfaces;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface NoteService {
    public CompletableFuture<NoteResponse> getNoteDetail(UUID id);
//    public CompletableFuture<Page<NoteResponse>> getNotesByCategory(UUID categoryId);
    public CompletableFuture<Page<NoteResponse>> searchNotes(String searchTerm, UUID userId, Pageable pageable);
    public CompletableFuture<NoteResponse> createNote(NoteRequest request, UUID userId);
    public CompletableFuture<NoteResponse> updateNote (UUID id, NoteRequest request, UUID userId);
    public CompletableFuture<Void> softDeleteNote(UUID id);
}
