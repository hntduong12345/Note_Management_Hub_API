package com.notemanagement.NoteManagementHubAPI.controllers;

import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.AuthResponse;
import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.LoginRequest;
import com.notemanagement.NoteManagementHubAPI.dtos.authdtos.RegisterRequest;
import com.notemanagement.NoteManagementHubAPI.exceptions.exceptionCases.InternalException;
import com.notemanagement.NoteManagementHubAPI.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController extends BaseController{
    private final UserService userService;

    @Operation(summary = "Login", description = "Login. Accessible Role: All")
    @ApiResponse(
            responseCode = "200",
            description = "Login successfully",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Login Failed: Invalid email or password."
    )
    @PostMapping("login")
    public CompletableFuture<AuthResponse> login(@RequestBody LoginRequest request){
        logger.info("Controller: login");
        return userService.login(request)
                .exceptionally(ex ->{
                    logger.error("Error when logging into a system");
                    throw new InternalException("Error when logging into a system", ex.getCause());
                });
    }

    @Operation(summary = "Register", description = "Register. Accessible Role: All")
    @ApiResponse(
            responseCode = "200",
            description = "Register successfully",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = AuthResponse.class)
            )
    )
    @PostMapping("register")
    public CompletableFuture<AuthResponse> register(@RequestBody RegisterRequest request){
        logger.info("Controller: register");
        return userService.register(request)
                .exceptionally(ex ->{
                    logger.error("Error when registering into a system");
                    throw new InternalException("Error when register into a system", ex.getCause());
                });
    }
}
