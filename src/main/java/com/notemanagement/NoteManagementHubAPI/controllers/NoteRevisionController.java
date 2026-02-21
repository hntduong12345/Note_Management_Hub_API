package com.notemanagement.NoteManagementHubAPI.controllers;

import com.notemanagement.NoteManagementHubAPI.dtos.notedtos.NoteResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionDetailResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.noterevisiondtos.NoteRevisionResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.NoteRevisionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("note-revisions")
@RequiredArgsConstructor
public class NoteRevisionController extends BaseController{
    private final NoteRevisionService noteRevisionService;

    @Operation(summary = "Get the note's history version list")
    @ApiResponse(
            responseCode = "200",
            description = "Get the note's history version list",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteRevisionResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping()
    public CompletableFuture<List<NoteRevisionResponse>> getRevisionHistory(@ParameterObject UUID noteId, @ParameterObject UUID userId){
        logger.info("Controller: Get the note's history version list");
        return noteRevisionService.getRevisionHistory(noteId, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when get the note's history version list");
                    throw new InternalException("Error occurs when get the note's history version list", ex.getCause());
                });
    }

    @Operation(summary = "Get the note's history version detail")
    @ApiResponse(
            responseCode = "200",
            description = "Get the note's history version detail",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteRevisionResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping("{id}")
    public CompletableFuture<NoteRevisionDetailResponse> getRevisionDetail(@PathVariable("id") UUID id, @ParameterObject UUID userId){
        logger.info("Controller: Get the note's history version detail");
        return noteRevisionService.getRevisionDetail(id, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when get the note's history version detail");
                    throw new InternalException("Error occurs when get the note's history version detail", ex.getCause());
                });
    }

    @Operation(summary = "Create a history version of note")
    @ApiResponse(
            responseCode = "200",
            description = "Create a history version of note",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Void.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PostMapping()
    public CompletableFuture<Void> createRevision(
            @ParameterObject UUID noteId,
            @ParameterObject UUID userId,
            @RequestBody NoteRevisionRequest request){
        logger.info("Controller: Create a history version of note");
        return noteRevisionService.createRevision(noteId, request, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when create a history version of note");
                    throw new InternalException("Error occurs when create a history version of note", ex.getCause());
                });
    }

    @Operation(summary = "Restore a history version of note")
    @ApiResponse(
            responseCode = "200",
            description = "Restore a history version of note",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PutMapping("{id}")
    public CompletableFuture<NoteResponse> createRevision(@PathVariable("id") UUID id, @ParameterObject UUID userId){
        logger.info("Controller: Restore a history version of note");
        return noteRevisionService.restoreRevision(id, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when restore a history version of note");
                    throw new InternalException("Error occurs when restore a history version of note", ex.getCause());
                });
    }
}
