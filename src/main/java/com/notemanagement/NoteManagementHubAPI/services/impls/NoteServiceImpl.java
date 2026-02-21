package com.notemanagement.NoteManagementHubAPI.services.impls;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.tagdtos.CreateTagRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.tagdtos.TagDTO;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.ConflictException;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.NotFoundException;
import com.notemanagement.NoteManagementHubAPI.models.Category;
import com.notemanagement.NoteManagementHubAPI.models.Note;
import com.notemanagement.NoteManagementHubAPI.models.Tag;
import com.notemanagement.NoteManagementHubAPI.models.User;
import com.notemanagement.NoteManagementHubAPI.repositories.CategoryRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.TagRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.UserRepository;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.NoteService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<NoteResponse> getNoteDetail(UUID id) {
        Note note = noteRepository.findById(id)
                .filter(n -> !n.isArchived())
                .orElseThrow(() -> new NotFoundException("Note is not found"));

        return CompletableFuture.completedFuture(mapToResponse(note));
    }

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<Page<NoteResponse>> searchNotes(String searchTerm, UUID userId, Pageable pageable) {
        return  CompletableFuture.supplyAsync(() -> {
            return noteRepository.searchNotes(userId, searchTerm, pageable)
                    .map(this::mapToResponse);
        });
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<NoteResponse> createNote(NoteRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Note newNote = new Note();
        newNote.setTitle(request.getTitle());
        newNote.setContentBody(request.getContentBody()); // The JSONB Map
        newNote.setUser(user);
        newNote.setArchived(false);

        //Check have category
        if(request.getCategoryId() != null){
            Category cate = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category is not found"));

            newNote.setCategory(cate);
        }

        //Sync tags
        if(request.getTagRequest() != null){
            newNote.setTags(syncTags(request.getTagRequest(), user));
        }

        Note savedNote = noteRepository.save(newNote);

        // Immediately sync the updatedAt value generate from the trigger in database
        entityManager.flush();
        entityManager.refresh(savedNote);

        return CompletableFuture.completedFuture(mapToResponse(savedNote));
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<NoteResponse> updateNote(UUID id, NoteRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note is not found"));

        //Check data sync between database and client
        if(request.getLastSyncAt() != null && note.getUpdatedAt().isAfter(request.getLastSyncAt())){
            log.warn("Conflict: The current Note value updated at {} but the request use the note at {}",
                    note.getUpdatedAt(), request.getLastSyncAt());
            throw new ConflictException("This note was updated on other device, please reload the note");
        }

        note.setTitle(request.getTitle());
        note.setContentBody(request.getContentBody());

        if(request.getCategoryId() != null){
            Category cate = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category is not found"));
             note.setCategory(cate);
        }

        if(request.getTagRequest() != null){
            note.getTags().clear();
            note.getTags().addAll(syncTags(request.getTagRequest(), user));
        }

        Note savedNote = noteRepository.save(note);

        // Immediately sync the updatedAt value generate from the trigger in database
        entityManager.flush();
        entityManager.refresh(savedNote);

        return CompletableFuture.completedFuture(mapToResponse(savedNote));
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> softDeleteNote(UUID id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note is not found"));

        note.setArchived(true);
        noteRepository.save(note);

        return CompletableFuture.completedFuture(null);
    }

    private Set<Tag> syncTags(List<CreateTagRequest> request, User user){
        if(request == null || request.isEmpty())
            return new HashSet<>();

        return request.stream()
                .map(req -> {
                    return tagRepository.findByNameAndUserId(req.getName(), user.getId())
                            .map(existTag -> {
                                existTag.setColorCode(req.getColor());
                                return tagRepository.save(existTag);
                            })
                            .orElseGet(() -> {
                                Tag newTag = new Tag();
                                newTag.setName(req.getName());
                                newTag.setColorCode(req.getColor());
                                newTag.setUser(user);
                                return tagRepository.save(newTag);
                            });
                }).collect(Collectors.toSet());
    }

    private NoteResponse mapToResponse(Note note){
        return new NoteResponse(
            note.getId(),
            note.getTitle(),
            note.getContentBody(),
            note.getCategory() != null ? note.getCategory().getId() : null,
            note.getTags().stream().map(t -> new TagDTO(t.getId(), t.getName(), t.getColorCode())).toList(),
            note.getUpdatedAt()
        );
    }
}
