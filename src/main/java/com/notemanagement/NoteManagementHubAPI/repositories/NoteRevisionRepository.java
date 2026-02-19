package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.NoteRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteRevisionRepository extends JpaRepository<NoteRevision, UUID> {
}
