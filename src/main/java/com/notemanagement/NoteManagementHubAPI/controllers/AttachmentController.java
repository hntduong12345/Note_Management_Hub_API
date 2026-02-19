package com.notemanagement.NoteManagementHubAPI.controllers;

import com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos.AttachmentResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.attachmentdtos.AttachmentUploadRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.userdtos.UserResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/attachments")
@RequiredArgsConstructor
public class AttachmentController extends BaseController{
    private final AttachmentService attachmentService;

    @Operation(summary = "Link the attachment of the note to database")
    @ApiResponse(
            responseCode = "200",
            description = "Link the attachment of the note to database",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AttachmentResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @PostMapping("users/{userId}")
    public CompletableFuture<AttachmentResponse> linkAttachment(@PathVariable("userId") UUID userId, @RequestBody AttachmentUploadRequest request){
        logger.info("Controller: Link attachment");
        return attachmentService.linkAttachment(request, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when link the note attachment to db");
                    throw new InternalException("Error occurs when link the note attachment to db", ex.getCause());
                });
    }

    @Operation(summary = "Delete the attachment of the note from database")
    @ApiResponse(
            responseCode = "200",
            description = "Delete the attachment of the note from database",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Void.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @DeleteMapping("{id}/users/{userId}")
    public CompletableFuture<Void> deleteAttachment(@PathVariable("id") UUID id, @PathVariable("userId") UUID userId){
        logger.info("Controller: Delete the attachment");
        return attachmentService.deleteAttachment(id, userId)
                .exceptionally(ex ->
                {
                    logger.error("Error occurs when delete the note attachment from db");
                    throw new InternalException("Error occurs when delete the note attachment from db", ex.getCause());
                });
    }
}
