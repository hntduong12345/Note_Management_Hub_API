package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.NoteRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NoteRevisionRepository extends JpaRepository<NoteRevision, UUID> {
    @Query("SELECT nr FROM NoteRevision nr WHERE nr.note.id = :noteId ORDER BY nr.createdAt DESC")
    List<NoteRevision> findAllByNoteId(UUID noteId);
}
