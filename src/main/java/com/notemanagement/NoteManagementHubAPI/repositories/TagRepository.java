package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {
    Optional<Tag> findByNameAndUserId(String name, UUID userId);
}
