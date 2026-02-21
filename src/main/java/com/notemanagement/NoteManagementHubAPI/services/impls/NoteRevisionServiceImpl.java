package com.notemanagement.NoteManagementHubAPI.services.impls;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionDetailResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.tagdtos.TagDTO;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.NotFoundException;
import com.notemanagement.NoteManagementHubAPI.models.Note;
import com.notemanagement.NoteManagementHubAPI.models.NoteRevision;
import com.notemanagement.NoteManagementHubAPI.models.User;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteRevisionRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.UserRepository;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.NoteRevisionService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteRevisionServiceImpl implements NoteRevisionService {
    private final NoteRevisionRepository noteRevisionRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    private final EntityManager entityManager;

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> createRevision(UUID noteId, NoteRevisionRequest request, UUID userId) {
        return CompletableFuture.runAsync(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User is not existing!"));
            Note note = noteRepository.findById(noteId)
                    .orElseThrow(() -> new NotFoundException("Note is not found"));

            NoteRevision newRevision = new NoteRevision();
            newRevision.setContentSnapshot(request.getContentBody());
            newRevision.setVersionNumber(request.getVersionNumber());
            newRevision.setNote(note);
            noteRevisionRepository.save(newRevision);
        });
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<NoteRevisionResponse>> getRevisionHistory(UUID noteId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note is not found"));

        return CompletableFuture.completedFuture(
                noteRevisionRepository.findAllByNoteId(noteId).stream()
                .map(revision -> new NoteRevisionResponse(
                        revision.getId(),
                        revision.getVersionNumber(),
                        revision.getCreatedAt()
                )).collect(Collectors.toList()));
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<NoteRevisionDetailResponse> getRevisionDetail(UUID revisionId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));
        NoteRevision revision = noteRevisionRepository.findById(revisionId)
                .orElseThrow(() -> new NotFoundException("Note Revision is not found"));

        NoteRevisionDetailResponse response = new NoteRevisionDetailResponse(
                revision.getId(),
                revision.getVersionNumber(),
                revision.getContentSnapshot(),
                revision.getCreatedAt()
        );
        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<NoteResponse> restoreRevision(UUID revisionId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));
        NoteRevision revision = noteRevisionRepository.findById(revisionId)
                .orElseThrow(() -> new NotFoundException("Note Revision is not found"));

        Note note = revision.getNote();
        note.setContentBody(revision.getContentSnapshot());

        Note updatedNote = noteRepository.save(note);

        entityManager.flush();
        entityManager.refresh(updatedNote);

        return CompletableFuture.completedFuture(
                new NoteResponse(
                        updatedNote.getId(),
                        updatedNote.getTitle(),
                        updatedNote.getContentBody(),
                        updatedNote.getCategory().getId(),
                        updatedNote.getTags().stream().map(t -> new TagDTO(t.getId(),t.getName(),t.getColorCode())).toList(),
                        updatedNote.getUpdatedAt()
                )
        );
    }
}
