package com.notemanagement.NoteManagementHubAPI.controllers;

import com.notemanagement.NoteManagementHubAPI.dtos.userdtos.UserResponse;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserController extends BaseController{
    private final UserService userService;

    @Operation(summary = "Get user detail by id", description = "Get user detail by id.")
    @ApiResponse(
            responseCode = "200",
            description = "Get user detail by id successfully",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login is required."
    )
    @GetMapping("{id}")
    public CompletableFuture<UserResponse> getUserDetail(@PathVariable("id") UUID id){
        logger.info("Controller: Get user detail by id");
        return userService.getProfile(id)
                .exceptionally(ex ->
                {
                    logger.error("Error when getting user by id from the system");
                   throw new InternalException("Error occurs when getting user information details", ex.getCause());
                });
    }
}
