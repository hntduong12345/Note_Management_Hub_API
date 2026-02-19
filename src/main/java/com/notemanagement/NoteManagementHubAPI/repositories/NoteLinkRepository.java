package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.NoteLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NoteLinkRepository extends JpaRepository<NoteLink, UUID> {
}
