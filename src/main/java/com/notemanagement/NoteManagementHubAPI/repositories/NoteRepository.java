package com.notemanagement.NoteManagementHubAPI.repositories;

import com.notemanagement.NoteManagementHubAPI.models.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {
    @Query(value = """ 
        SELECT * FROM notes 
        WHERE user_id = :userId 
        AND is_archived = false 
        AND (:keyword IS NULL OR (title ILIKE %:keyword% OR content_body->>'text' ILIKE %:keyword%))
        """, countQuery = """
        SELECT COUNT(*) FROM notes 
        WHERE user_id = :userId 
        AND is_archived = false 
        AND (:keyword IS NULL OR (title ILIKE %:keyword% OR content_body->>'text' ILIKE %:keyword%))
        """, nativeQuery = true)
    Page<Note> searchNotes(
            @Param("userId") UUID userId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Note n SET n.category.id = :newCategoryId WHERE n.category.id = :oldCategoryId AND n.user.id = :userId")
    void moveNotesToNewCategory(
            @Param("oldCategoryId") UUID oldCategoryId,
            @Param("newCategoryId") UUID newCategoryId,
            @Param("userId") UUID userId
    );
}
