package com.notemanagement.NoteManagementHubAPI.controllers;

import com.notemanagement.NoteManagementHubAPI.constants.ApiPath;
import com.notemanagement.NoteManagementHubAPI.dtos.commons.PageResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ApiPath.BASEURL + "/notes")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class NoteController extends BaseController{
    private final NoteService noteService;

    @Operation(summary = "Get the note detail")
    @ApiResponse(
            responseCode = "200",
            description = "Get the note detail by its id",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping("{id}")
    public CompletableFuture<NoteResponse> getNoteDetail(@PathVariable("id") UUID id){
        logger.info("Controller: Get note detail");
        return noteService.getNoteDetail(id)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when get note detail");
                    throw new InternalException("Error occurs when get note detail", ex.getCause());
                });
    }

    @Operation(summary = "Search the note")
    @ApiResponse(
            responseCode = "200",
            description = "Search the note by title or body content",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PageResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping("users/{userId}/searching")
    public CompletableFuture<PageResponse<NoteResponse>> searchNotes(
            @PathVariable("userId") UUID userId,
            @RequestParam(required = false) String searchTerm,
            @ParameterObject Pageable pageable){
        logger.info("Controller: Search note");
        return noteService.searchNotes(searchTerm, userId, pageable)
                .thenApply(PageResponse::from)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when search note");
                    throw new InternalException("Error occurs when search note", ex.getCause());
                });
    }

    @Operation(summary = "Create new note")
    @ApiResponse(
            responseCode = "200",
            description = "Create a new note",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PostMapping("users/{userId}")
    public CompletableFuture<NoteResponse> createNote(@PathVariable("userId") UUID userId, @RequestBody NoteRequest request){
        logger.info("Controller: Create note");
        return noteService.createNote(request, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when create note");
                    throw new InternalException("Error occurs when create note", ex.getCause());
                });
    }

    @Operation(summary = "Update note")
    @ApiResponse(
            responseCode = "200",
            description = "Update an existing note",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PutMapping("{id}/users/{userId}")
    public CompletableFuture<NoteResponse> updateNote(
            @PathVariable("id") UUID id,
            @PathVariable("userId") UUID userId,
            @RequestBody NoteRequest request){
        logger.info("Controller: Update note");
        return noteService.updateNote(id, request, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when update note");
                    throw new InternalException("Error occurs when update note", ex.getCause());
                });
    }

    @Operation(summary = "Soft delete note")
    @ApiResponse(
            responseCode = "200",
            description = "Temporary hide the chosen note",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Void.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PatchMapping("{id}")
    public CompletableFuture<Void> softDeleteNote(@PathVariable("id") UUID id){
        logger.info("Controller: Soft delete note");
        return noteService.softDeleteNote(id)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when soft delete note");
                    throw new InternalException("Error occurs when soft delete note", ex.getCause());
                });
    }
}
