package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.NoteLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteLinkRepository extends JpaRepository<NoteLink, UUID> {
    //Find all the link from this note
    @Query("""
        SELECT nl FROM NoteLink nl 
        JOIN FETCH nl.sourceNote    
        JOIN FETCH nl.targetNote    
        WHERE nl.sourceNote.id = :noteId
    """)
    List<NoteLink> findAllBySourceNoteId(UUID noteId);

    //Find all the link point to this note
    @Query("""
        SELECT nl FROM NoteLink nl 
        JOIN FETCH nl.sourceNote    
        JOIN FETCH nl.targetNote    
        WHERE nl.targetNote.id = :noteId
    """)
    List<NoteLink> findAllByTargetNoteId(UUID noteId);

    Optional<NoteLink> findBySourceNoteId(UUID sourceNoteId);
    Optional<NoteLink> findByTargetNoteId(UUID targetNoteId);
}
