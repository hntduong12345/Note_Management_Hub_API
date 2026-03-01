package com.notemanagement.NoteManagementHubAPI.controllers;

import com.cloudinary.Cloudinary;
import com.notemanagement.NoteManagementHubAPI.constants.ApiPath;
import com.notemanagement.NoteManagementHubAPI.dtos.categorydtos.CategoryRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.categorydtos.CategoryResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ApiPath.BASEURL + "/categories")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CategoryController extends BaseController{
    private final CategoryService categoryService;
    private final Cloudinary cloudinary;

    @Operation(summary = "Get user's all categories")
    @ApiResponse(
            responseCode = "200",
            description = "Get all the categories that user has created",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CategoryResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping()
    public CompletableFuture<List<CategoryResponse>> getAllCategories(@ParameterObject UUID userId){
        logger.info("Controller: Get all user's categories");
        return categoryService.getAllCategories(userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when get all user's categories");
                    throw new InternalException("Error occurs when get all user's categories", ex.getCause());
                });
    }

    @Operation(summary = "Create category")
    @ApiResponse(
            responseCode = "200",
            description = "Create a new category",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CategoryResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CompletableFuture<CategoryResponse> createCategory(
            @ParameterObject UUID userId,
            @RequestParam("name") String name,
            @RequestParam("file") MultipartFile file){
        logger.info("Controller: Create category");
        System.out.println("Test Test");
        System.out.println(
                SecurityContextHolder.getContext().getAuthentication()
        );

        return categoryService.createCategory(name, file, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when create category");
                    throw new InternalException("Error occurs when create category", ex.getCause());
                });
    }

    @Operation(summary = "Update category")
    @ApiResponse(
            responseCode = "200",
            description = "Update a new category",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CategoryResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PutMapping("{id}")
    public CompletableFuture<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @ParameterObject UUID userId,
            @RequestBody CategoryRequest request){
        logger.info("Controller: Update category");
        return categoryService.updateCategory(id, request, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when update category");
                    throw new InternalException("Error occurs when update category", ex.getCause());
                });
    }

    @Operation(summary = "Delete category")
    @ApiResponse(
            responseCode = "200",
            description = "Delete an existing category, if category have notes all notes will move to 'Uncategorized' category",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Void.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @DeleteMapping("{id}")
    public CompletableFuture<Void> deleteCategory(@PathVariable UUID id, @ParameterObject UUID userId){
        logger.info("Controller: Delete category");
        return categoryService.deleteCategory(id, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when delete category");
                    throw new InternalException("Error occurs when delete category", ex.getCause());
                });
    }
}
