package com.notemanagement.NoteManagementHubAPI.services.impls;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos.NoteLinkRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos.NoteLinkResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.NotFoundException;
import com.notemanagement.NoteManagementHubAPI.models.Note;
import com.notemanagement.NoteManagementHubAPI.models.NoteLink;
import com.notemanagement.NoteManagementHubAPI.models.User;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteLinkRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.UserRepository;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.NoteLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteLinkServiceImpl implements NoteLinkService {
    private final NoteLinkRepository noteLinkRepository;
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<NoteLinkResponse> createLink(NoteLinkRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User is not existing!"));

        Note sourceNote = noteRepository.findByIdAndUserId(request.getSourceId(), request.getUserId())
                .orElseThrow(() -> new NotFoundException("Source note of current user is not found"));
        Note targetNote = noteRepository.findByIdAndUserId(request.getTargetId(), request.getUserId())
                .orElseThrow(() -> new NotFoundException("Target note of current user is not found"));

        NoteLink noteLink = new NoteLink();
        noteLink.setSourceNote(sourceNote);
        noteLink.setTargetNote(targetNote);
        noteLink.setLinkContext(request.getLinkContext());
        NoteLink savedLink = noteLinkRepository.save(noteLink);

        return CompletableFuture.completedFuture(mapToResponse(savedLink, true));
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> removeLink(UUID noteLinkId, UUID userId) {
        return CompletableFuture.runAsync(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User is not existing!"));

            NoteLink link = noteLinkRepository.findById(noteLinkId)
                    .orElseThrow(() -> new NotFoundException("Note link is not found"));

            noteLinkRepository.delete(link);
        });
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    //Get all the notes that the currentNote link to
    public CompletableFuture<List<NoteLinkResponse>> getTargetLinks(UUID noteId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));
        NoteLink link = noteLinkRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note link is not found"));

        return CompletableFuture.completedFuture(
                noteLinkRepository.findAllBySourceNoteId(noteId)
                    .stream().map(noteLink -> mapToResponse(noteLink, true))
                    .collect(Collectors.toList()));
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    //Get all the notes that link to the current note
    public CompletableFuture<List<NoteLinkResponse>> getSourceLinks(UUID noteId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));
        NoteLink link = noteLinkRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note link is not found"));

        return CompletableFuture.completedFuture(
                noteLinkRepository.findAllByTargetNoteId(noteId)
                    .stream().map(noteLink -> mapToResponse(noteLink, false))
                    .collect(Collectors.toList()));
    }

    private NoteLinkResponse mapToResponse(NoteLink noteLink, boolean getTarget){
        return new NoteLinkResponse(
                noteLink.getId(),
                noteLink.getSourceNote().getId(),
                noteLink.getTargetNote().getId(),
                noteLink.getLinkContext(),
                noteLink.getCreatedAt(),
                getTarget ? noteLink.getTargetNote().getTitle() : noteLink.getSourceNote().getTitle()
        );
    }
}
