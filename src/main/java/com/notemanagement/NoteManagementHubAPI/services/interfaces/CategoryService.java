package com.notemanagement.NoteManagementHubAPI.services.interfaces;

import com.notemanagement.NoteManagementHubAPI.dtos.categorydtos.CategoryRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.categorydtos.CategoryResponse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface CategoryService {
    CompletableFuture<List<CategoryResponse>> getAllCategories(UUID userId);
    CompletableFuture<CategoryResponse> createCategory(CategoryRequest request, UUID userId);
    CompletableFuture<CategoryResponse> updateCategory(UUID id, CategoryRequest request, UUID userId);
    CompletableFuture<Void> deleteCategory(UUID id, UUID userId);
}
