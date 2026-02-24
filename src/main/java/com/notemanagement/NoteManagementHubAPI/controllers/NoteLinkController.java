package com.notemanagement.NoteManagementHubAPI.controllers;

import com.notemanagement.NoteManagementHubAPI.constants.ApiPath;
import com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos.NoteLinkRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.notelinkdtos.NoteLinkResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.NoteLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(ApiPath.BASEURL + "/note-links")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class NoteLinkController extends BaseController{
    private final NoteLinkService noteLinkService;

    @Operation(summary = "Get all current note's target note links")
    @ApiResponse(
            responseCode = "200",
            description = "Get all notes that the current note links to (Current is the Source, Result is the link target)",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteLinkResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping("targets")
    public CompletableFuture<List<NoteLinkResponse>> getTargetNoteLinks(
            @ParameterObject UUID noteId,
            @ParameterObject UUID userId){
        logger.info("Controller: Get all current note's target note links");
        return noteLinkService.getTargetLinks(noteId, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when get all current note's target note links");
                    throw new InternalException("Error occurs when get all current note's target note links", ex.getCause());
                });
    }

    @Operation(summary = "Get all current note's source note links")
    @ApiResponse(
            responseCode = "200",
            description = "Get all notes that links the current note (Current is the link target, Result is the source)",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteLinkResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping("sources")
    public CompletableFuture<List<NoteLinkResponse>> getSourceNoteLinks(
            @ParameterObject UUID noteId,
            @ParameterObject UUID userId){
        logger.info("Controller: Get all current note's source note links");
        return noteLinkService.getSourceLinks(noteId, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when get all current note's source note links");
                    throw new InternalException("Error occurs when get all current note's source note links", ex.getCause());
                });
    }

    @Operation(summary = "Create a note link")
    @ApiResponse(
            responseCode = "200",
            description = "Create the note link between source and target",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = NoteLinkResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PostMapping()
    public CompletableFuture<NoteLinkResponse> createNoteLink(@RequestBody NoteLinkRequest request){
        logger.info("Controller: Create note link");
        return noteLinkService.createLink(request)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when create note link");
                    throw new InternalException("Error occurs when create note link", ex.getCause());
                });
    }

    @Operation(summary = "Remove a note link")
    @ApiResponse(
            responseCode = "200",
            description = "Remove the note link between source and target",
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
    public CompletableFuture<Void> removeNoteLink(@PathVariable("id") UUID id, @ParameterObject UUID userId){
        logger.info("Controller: Remove note link");
        return noteLinkService.removeLink(id, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when remove note link");
                    throw new InternalException("Error occurs when remove note link", ex.getCause());
                });
    }
}
