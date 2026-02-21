package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByUserIdOrderByName(UUID userId);
    Optional<Category> findByNameAndUserId(String name, UUID userId);

}
