package com.notemanagement.NoteManagementHubAPI.services.impls;

import com.notemanagement.NoteManagementHubAPI.dtos.categorydtos.CategoryRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.categorydtos.CategoryResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.ConflictException;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.NotFoundException;
import com.notemanagement.NoteManagementHubAPI.models.Category;
import com.notemanagement.NoteManagementHubAPI.models.User;
import com.notemanagement.NoteManagementHubAPI.repositories.CategoryRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.NoteRepository;
import com.notemanagement.NoteManagementHubAPI.repositories.UserRepository;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.CategoryService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    private final EntityManager entityManager;

    private static final String DEFAULT_CATEGORY_NAME = "Uncategorized";

    @Override
    @Async("taskExecutor")
    @Transactional(readOnly = true)
    public CompletableFuture<List<CategoryResponse>> getAllCategories(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));

        List<CategoryResponse> response = categoryRepository.findAllByUserIdOrderByName(userId)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());

        return CompletableFuture.completedFuture(response);
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<CategoryResponse> createCategory(CategoryRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));

        Category newCate = new Category();
        newCate.setName(request.getName());
        newCate.setIconIdentifier(request.getIconIdentifier());

        Category savedCate = categoryRepository.save(newCate);

        // Immediately sync the updatedAt value generate from the trigger in database
        entityManager.flush();
        entityManager.refresh(savedCate);

        return CompletableFuture.completedFuture(mapToResponse(savedCate));
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<CategoryResponse> updateCategory(UUID id, CategoryRequest request, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not existing!"));

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category is not found"));

        category.setName(request.getName());
        category.setIconIdentifier(request.getIconIdentifier());

        Category savedCategory = categoryRepository.save(category);

        // Immediately sync the updatedAt value generate from the trigger in database
        entityManager.flush();
        entityManager.refresh(savedCategory);

        return CompletableFuture.completedFuture(mapToResponse(savedCategory));
    }

    @Override
    @Async("taskExecutor")
    @Transactional
    public CompletableFuture<Void> deleteCategory(UUID id, UUID userId) {
        return CompletableFuture.runAsync(() -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User is not existing!"));

            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Category is not found"));

            if(category.getName().equalsIgnoreCase(DEFAULT_CATEGORY_NAME)){
                throw new ConflictException("Cannot delete the default category of the system!");
            }

            //Check user has already had a default category
            Category defaultCategory = categoryRepository.findByNameAndUserId(DEFAULT_CATEGORY_NAME, userId)
                    .orElseGet(() -> {
                        Category newDefault = new Category();
                        newDefault.setName(DEFAULT_CATEGORY_NAME);
                        newDefault.setIconIdentifier("");
                        newDefault.setUser(user);
                        return categoryRepository.save(newDefault);
                    });

            //Change all the notes of deleted category to the default category
            noteRepository.moveNotesToNewCategory(category.getId(), defaultCategory.getId(), userId);

            categoryRepository.delete(category);
        });
    }

    private CategoryResponse mapToResponse(Category category){
        return new CategoryResponse(
              category.getId(),
              category.getName(),
              category.getIconIdentifier(),
              category.getCreatedAt()
        );
    }
}
